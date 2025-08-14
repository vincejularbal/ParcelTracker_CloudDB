# 📦 Parcel Tracker Android App with Cloud Database
**Module 3 – Mobile App (Kotlin, Android Studio, Cloud Database)**

### 📌 Developer: Vincent Jularbal  
**Course:** CSE 310 – Applied Programming  
**Sprint Week:** July 2025  
**Module Selected:** Cloud Database (Module 3)

---

# Overview

This project is an Android mobile application developed in Kotlin that functions as a **Parcel Tracking System** with cloud database integration. It allows users to add parcels, generate and scan QR codes for quick parcel lookup, update delivery status, view tracking history, and securely sign in using Firebase Authentication. All parcel data and tracking histories are stored in **Firebase Cloud Firestore**, ensuring that data is persistent and accessible from any device.

The goal of this software is to gain practical experience integrating Android applications with a cloud backend, implementing user authentication, and handling real-time data synchronization. The app is designed for logistics and delivery tracking scenarios but can be adapted for other use cases where item tracking is needed.

**How to use:**
1. Sign in or create an account using Email/Password authentication.
2. Add a new parcel with a tracking number, recipient name, and address.
3. The app automatically generates a QR code for the parcel.
4. Scan a QR code to instantly open the parcel's record.
5. Update parcel status and location as needed.
6. View tracking history for detailed progress logs.
7. Sign out when finished.

[Software Demo Video](https://youtu.be/c5sp87YkjsU)

---

# Cloud Database

The app uses **Firebase Cloud Firestore** as its backend database.  
**Structure:**
- **`parcels`** (Collection)
  - `trackingNumber` (String)
  - `recipientName` (String)
  - `address` (String)
  - `qrBitmap` (Stored/generated locally, not in DB)
- **`trackingHistory`** (Subcollection per parcel)
  - `timestamp` (Date/Time)
  - `status` (String)
  - `location` (String)

Firestore is configured to allow authenticated users to read/write only their own data, ensuring data security.

---

# Development Environment

- **IDE:** Android Studio
- **Language:** Kotlin
- **Platform:** Android (Min SDK 26, Target SDK 36)
- **Cloud Services:** Firebase Authentication, Firebase Cloud Firestore
- **Libraries:**
  - [Firebase Auth KTX](https://firebase.google.com/docs/auth/android/start)
  - [Firebase Firestore KTX](https://firebase.google.com/docs/firestore/quickstart)
  - [FirebaseUI Auth](https://github.com/firebase/FirebaseUI-Android) for simplified sign-in flow
  - ZXing Android Embedded for QR code generation and scanning
  - AndroidX RecyclerView, CardView, AppCompat, Material Components

---

# Useful Websites

- [Firebase Documentation](https://firebase.google.com/docs)
- [FirebaseUI for Android](https://github.com/firebase/FirebaseUI-Android)
- [ZXing QR Code Library](https://github.com/journeyapps/zxing-android-embedded)
- [Android Developer Documentation](https://developer.android.com/docs)

---

# Future Work

- Implement push notifications for status updates.
- Add support for parcel images and file attachments.
- Improve UI design for better user experience.
- Add search and filtering capabilities for large parcel lists.
