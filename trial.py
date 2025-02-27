import sounddevice as sd
import numpy as np
import wave
import deepspeech
import requests
from pypinyin import lazy_pinyin  # Converts Chinese to Pinyin

# Load DeepSpeech model
model_file = r"A:\Downloads\Thesis\English-To-Chinese-For-Testing\English\deepspeech-0.9.3-models.pbmm"
ds = deepspeech.Model(model_file)

# LibreTranslate API
LIBRETRANSLATE_URL = "http://localhost:5000/translate"
SOURCE_LANG = "en"  # English
TARGET_LANG = "zh"  # Chinese (Simplified)

# Record audio from microphone
def record_audio(duration=5, samplerate=16000):
    print("Recording...")
    audio = sd.rec(int(duration * samplerate), samplerate=samplerate, channels=1, dtype=np.int16)
    sd.wait()
    print("Recording finished.")
    return audio

# Normalize audio
def normalize_audio(audio_data):
    audio_data = audio_data.astype(np.float32)
    audio_data /= np.max(np.abs(audio_data))
    return (audio_data * 32767).astype(np.int16)

# Transcribe speech to text
def transcribe_audio(audio_data, samplerate=16000):
    wav_filename = "temp.wav"
    audio_data = normalize_audio(audio_data)

    with wave.open(wav_filename, 'wb') as wf:
        wf.setnchannels(1)
        wf.setsampwidth(2)
        wf.setframerate(samplerate)
        wf.writeframes(audio_data.tobytes())

    with wave.open(wav_filename, 'rb') as wf:
        frames = wf.readframes(wf.getnframes())

    audio_buffer = np.frombuffer(frames, dtype=np.int16)
    text = ds.stt(audio_buffer).strip()  # Remove extra spaces
    return text if text else None  # Return None if no text detected

# Translate text
def translate_text(text, source_lang=SOURCE_LANG, target_lang=TARGET_LANG):
    # Debugging: Print raw text
    print(f"Raw Transcription Output: '{text}'")

    # Force sentence context if input is too short
    if len(text.split()) < 5:
        text += "."  

    response = requests.post(
        LIBRETRANSLATE_URL,
        data={"q": text, "source": source_lang, "target": target_lang}
    )

    if response.status_code == 200:
        translated_text = response.json().get("translatedText", "").strip()
        
        if not translated_text:
            return "Translation failed", ""

        # Convert to Pinyin if Chinese
        if target_lang == "zh":
            pinyin_text = " ".join([word.strip() for word in lazy_pinyin(translated_text) if word.strip()])
            return translated_text, pinyin_text  # Return both Chinese & Pinyin
        
        return translated_text, ""  # If not Chinese, no Pinyin
    
    return f"Error: {response.status_code}, {response.text}", ""

# Process translation
audio_data = record_audio(duration=5)
transcription = transcribe_audio(audio_data)

if transcription:
    print("Transcription:", transcription)

    # Translate & get Pinyin
    translated_text, pinyin_text = translate_text(transcription)
    print("Translation:", translated_text)
    
    if pinyin_text:
        print("Pinyin:", pinyin_text)
else:
    print("Transcription failed. Try speaking more clearly or checking your microphone.")
