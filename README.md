# PakStyleLens

PakStyleLens is a **fashion search and recommendation app** built with **Kotlin (Android)** and integrated with an AI-powered backend.
The app allows users to search for clothing items by **caption (text)** or by **uploading an image**, returning similar or relevant outfit suggestions.

---

## Features

* **Search by Caption** → Type in text like *“red kurta with embroidery”* and get relevant results.
* **Search by Image** → Upload a clothing image and find similar items.
* **Simple UI/UX** → Built using `RecyclerView`, `Dialogs`, `Toasts`, and `Material UI`.
* **AI Integration** → Backend powered by **YOLOv8** for detection + **FashionCLIP** for image-text similarity.
* **User Input** → Uses `EditText`, `ImageView`, and `Buttons` for easy interactions.
* **Fast & Lightweight** → Optimized resource usage with proper activity lifecycle handling.

---

## Tech Stack

### Frontend (Mobile)

* **Language:** Kotlin
* **Framework:** Android SDK
* **UI:** RecyclerView, ConstraintLayout, XML layouts
* **Navigation:** Activities + Intents

## Backend (fashionclip-api)

PakStyleLens is powered by a separate backend service: **[fashionclip-api](https://github.com/NabeehaAliii/fashionclip-api)**.

### Responsibilities of the backend:

* **AI Inference**
  * Uses **YOLOv8** to detect clothing items in uploaded images.
  * Uses **FashionCLIP** (fine-tuned with Eastern fashion data) to generate embeddings for text + image similarity.

* **API Endpoints**
  * `/search/caption` → Takes user text input and returns matching clothing items.
  * `/search/image` → Accepts an uploaded image and returns similar fashion items.

* **Data Management**
  * Custom dataset scraped from Pakistani fashion brands (Khaadi, Limelight, Sana Safinaz, etc.).
  * Cleaned, structured, and stored in the backend.

### How the app communicates with it:

* The Android frontend uses **Retrofit** (HTTP client for Kotlin/Android) to call the backend APIs.
* Responses (JSON) are displayed in the app via `RecyclerView`.
---

## Project Structure

```
pakstylelens/
 ├── app/                # Main app code (activities, adapters, utils)
 │    ├── src/main/java  # Kotlin source code
 │    ├── res/layout     # XML UI layouts
 │    └── res/drawable   # Images & icons
 ├── gradle/             # Gradle wrapper
 ├── build.gradle.kts    # Gradle build config
 ├── settings.gradle.kts # Gradle settings
 └── .gitignore          # Git ignore rules
```

---

## Getting Started

### Prerequisites

* Android Studio (latest version)
* JDK 11 or above
* Emulator / physical Android device

### Installation

1. Clone the repo:

   ```bash
   git clone https://github.com/NabeehaAliii/pakstylelens.git
   ```
2. Open in **Android Studio**.
3. Sync Gradle and build the project.
4. Run on emulator or connected device.

---

## Screenshots

(Add screenshots of your app here – home screen, search results, upload flow)
<img width="1604" height="725" alt="image" src="https://github.com/user-attachments/assets/35fb3297-11ca-4113-9c9f-fb225e0218ae" />
<img width="1604" height="725" alt="image" src="https://github.com/user-attachments/assets/3a63783f-5dc8-4ad6-b037-be66e67f5071" />

---

## Contributing

Contributions are welcome!

1. Fork the repo
2. Create a feature branch (`git checkout -b feature-name`)
3. Commit changes (`git commit -m 'Add new feature'`)
4. Push and open a Pull Request

---

## License

This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details.

---

## Authors

* **Nabeeha Ali** – [GitHub Profile](https://github.com/NabeehaAliii)

---

*PakStyleLens is a step towards blending AI with fashion – making outfit discovery simple, smart, and fun.*

