# 📦 Parcel Tracker Android App  
**Module 2 – Mobile App (Kotlin, Android Studio)**

### 📌 Developer: Vincent Jularbal  
**Course:** CSE 310 – Applied Programming  
**Sprint Week:** July 2025  
**Module Selected:** Mobile App (Module 2)

---

## 📄 Project Description

This project is a Kotlin-based Android mobile application designed to simulate a **Parcel Tracking System**. Users can add parcel records, update their delivery status and hub location, and view detailed tracking histories. The app also includes a **QR code generation and scanning** feature, allowing fast access to parcels using their tracking numbers.

---

## ✅ Features Implemented

- Add parcel records (tracking number, recipient, and address)
- Generate and display QR code for each parcel
- View all parcels in a card-based RecyclerView list
- Update parcel status and hub location
- Scan QR codes to find and open a parcel instantly
- View detailed tracking history with timestamps
- Delete parcels with confirmation

---

## 📱 Technical Overview

- **Language:** Kotlin  
- **Platform:** Android (Min SDK 26, Target SDK 34)  
- **IDE:** Android Studio  
- **Libraries Used:**  
  - ZXing Android Embedded (QR code scanning/generation)  
  - AndroidX RecyclerView, CardView, AppCompat  
  - Standard Android SDK components (Intent, Spinner, etc.)
- **Storage:** In-memory only (via `MutableList`). Data persistence is not implemented for this module.

---

## 📸 Demo Video

*Link to 4–5 minute software demo and walkthrough:*  
(https://youtu.be/fXipWbvp31k)

---

## 📦 Requirements Met

**✔ Basic Module Requirements**
- App includes interactive UI screens
- Accepts user input via forms and lists
- Displays user output clearly in a scrollable list

**✔ Unique Requirement Chosen**
- Integrates with device camera to **scan a QR code** that holds a parcel’s tracking number and navigates to its record

---

## 🛠 Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/parcel-tracker-android.git
2. Open in Android Studio
3. Sync Gradle (required libraries: ZXing, AndroidX)
4. Run the app using an emulator or physical device with camera access
