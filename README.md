# KotlinTracker

An Android app written in Kotlin for tracking packages and letters. The sender and receiver each scan a QR code on the item, which connects them, and they can then message each other about the delivery.

## Features

- Register with a profile photo, and log in with Firebase Authentication
- **Scan a QR code** on a package to link the sender and the receiver
- **Chat**: start a conversation with a user, see a chat log and a "latest messages" list
- Profile images stored in Firebase Storage

## Tech stack

- **Kotlin**, Android SDK (AndroidX, ConstraintLayout)
- **Firebase** Authentication, Realtime Database and Storage
- Camera-based barcode/QR scanning

## Project structure

```
app/src/main/java/com/example/kotlintracker/
  registerlogin/   RegisterActivity, LoginActivity
  scanning/        BarcodeScan
  messages/        LatestMessagesActivity, NewMessageActivity, ChatLogActivity
  views/           LatestMessageRow
  models/          User, ChatMessage
```

## Getting started

1. Open the project in Android Studio.
2. Add your Firebase `google-services.json` to `app/`.
3. Sync Gradle and run on a device with a camera.
