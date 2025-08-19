# PadeliumMarhaba 🏨🏓
**Exclusive Padel Court Reservation App for Hotel Padelium Marhaba - Sousse**

PadeliumMarhaba is a dedicated mobile application for padel court reservations at the prestigious Hotel Padelium Marhaba in Sousse, Tunisia. This exclusive app provides hotel guests and members with seamless access to world-class padel facilities in a luxury resort setting.

## 🌟 Features

### For Hotel Guests & Members
- **Exclusive Court Access**: Reserve premium padel courts at Hotel Padelium Marhaba
- **Real-time Availability**: Check court availability 24/7 with instant updates
- **Guest Priority Booking**: Hotel guests enjoy priority reservation privileges
- **Equipment Rental**: Book padel rackets and balls directly through the app
- **Coaching Sessions**: Schedule professional padel coaching with certified instructors
- **Tournament Participation**: Join hotel-organized padel tournaments and events
- **Integrated Hotel Services**: Link reservations with room service and spa bookings
- **Weather Updates**: Real-time weather conditions for optimal playing times
- **Multi-language Support**: Arabic, French, and English for international guests

### For Hotel Staff & Management
- **Guest Management**: Verify guest status and manage reservations
- **Court Maintenance**: Schedule and track court maintenance activities
- **Event Organization**: Organize padel tournaments and special events
- **Revenue Analytics**: Track padel facility usage and revenue
- **Guest Communications**: Send updates and promotional offers
- **Instructor Scheduling**: Manage coaching staff and lesson bookings

## 🏨 About Hotel Padelium Marhaba

Located in the heart of Sousse, Hotel Padelium Marhaba features:
- **4 Premium Padel Courts** with professional lighting
- **Luxury Amenities** including locker rooms and pro shop
- **Beachfront Location** with stunning Mediterranean views
- **Professional Coaching Staff** available for all skill levels
- **Tournament-Grade Facilities** hosting regional competitions

## 🚀 Technology Stack

- **Platform**: Android (Kotlin/Java)
- **Architecture**: MVVM with Clean Architecture
- **Database**: Room Database with SQLite
- **Networking**: Retrofit for API calls
- **Authentication**: Firebase Authentication + Hotel Guest Verification
- **Hotel Integration**: PMS (Property Management System) API
- **Push Notifications**: Firebase Cloud Messaging
- **Payment**: Integrated with hotel billing system
- **Weather API**: Real-time weather data integration

## 📱 Screenshots

*Coming soon - Add your app screenshots here*

## 🛠️ Installation & Setup

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK API level 21 or higher
- Google Play Services
- Firebase account for authentication and messaging
- Hotel PMS API access credentials

### Clone the Repository
```bash
git clone https://github.com/ahmed12112000/PadeliumMarhaba.git
cd PadeliumMarhaba
```

### Setup Firebase
1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add your Android app to the Firebase project
3. Download `google-services.json` and place it in the `app/` directory
4. Enable Authentication and Cloud Messaging in Firebase console

### Hotel System Integration
1. Obtain API credentials from Hotel Padelium Marhaba management
2. Configure PMS integration in `config.properties`:
```properties
HOTEL_PMS_BASE_URL=https://padelium-marhaba-api.com/
HOTEL_API_KEY=your_hotel_api_key
GUEST_VERIFICATION_ENDPOINT=/api/guests/verify
```

### API Configuration
1. Copy `config.example.properties` to `config.properties`
2. Add your API endpoints and keys:
```properties
BASE_URL=https://padelium-marhaba-api.com/
WEATHER_API_KEY=your_weather_api_key
FIREBASE_SERVER_KEY=your_firebase_server_key
HOTEL_BILLING_API_KEY=your_billing_api_key
```

### Build and Run
```bash
./gradlew assembleDebug
```

## 🏗️ Project Structure

```
app/
├── src/main/
│   ├── java/com/padelium/marhaba/
│   │   ├── ui/              # UI components and activities
│   │   ├── data/            # Repository, database, and API
│   │   ├── domain/          # Business logic, use cases, and domain models
│   │   ├── model/           # Data models
│   │   ├── utils/           # Utility classes
│   │   └── di/              # Dependency injection
│   ├── res/                 # Resources (layouts, strings, etc.)
│   └── AndroidManifest.xml
├── build.gradle
└── proguard-rules.pro
```

## 🏨 Hotel-Specific Features

### Guest Verification System
- Automatic guest status verification through hotel PMS
- Room number integration for easy booking
- Special rates for hotel guests vs. external members

### Integrated Billing
- Charges automatically added to hotel room bill
- Corporate account billing for group bookings
- Membership payment processing

### Concierge Integration
- Direct communication with hotel concierge
- Special event notifications
- VIP guest priority handling

## 🔧 Key Dependencies

```gradle
dependencies {
    implementation 'androidx.navigation:navigation-fragment-ktx:2.7.5'
    implementation 'com.google.android.material:material:1.10.0'
    implementation 'androidx.room:room-runtime:2.6.1'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.google.firebase:firebase-auth:22.3.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
    implementation 'androidx.work:work-runtime-ktx:2.9.0'
}
```

## 🤝 Contributing

We welcome contributions to improve PadeliumMarhaba! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow Android Kotlin style guidelines
- Use meaningful variable and function names
- Add comments for complex logic
- Ensure all new features have appropriate tests
- Follow hotel branding guidelines for UI elements

## 📄 API Documentation

The app connects to both hotel PMS and padel facility APIs:

### Padel Court API
```
GET /api/courts              # Get court availability
POST /api/bookings           # Create new booking
GET /api/bookings/{guestId}  # Get guest bookings
PUT /api/bookings/{id}       # Modify booking
DELETE /api/bookings/{id}    # Cancel booking
```

### Hotel Integration API
```
GET /api/guests/{roomNumber} # Verify guest status
POST /api/billing/charge     # Add charges to room bill
GET /api/events              # Get hotel padel events
```

## 🔐 Security & Privacy

- Guest data protection compliant with hotel privacy policies
- Secure integration with hotel PMS system
- Encrypted payment processing through hotel billing
- GDPR compliant data handling for international guests
- Automatic data cleanup for checked-out guests

## 📋 Roadmap

### Version 2.0
- [ ] Spa and wellness package integration
- [ ] Restaurant reservation linking
- [ ] Beach club coordination
- [ ] Loyalty program for repeat guests
- [ ] Multi-hotel expansion (other Padelium properties)

### Version 2.1
- [ ] AI-powered playing partner matching
- [ ] Advanced tournament management
- [ ] Social media integration
- [ ] Augmented reality court navigation
- [ ] Wearable device integration

## 🐛 Known Issues

- Occasional sync delays with hotel PMS during peak check-in times
- Weather data may be delayed during extreme weather conditions
- Room billing updates may take 30 minutes to reflect

## 📞 Support

### Technical Support
- **Email**: tech-support@padelium-marhaba.com
- **In-Hotel**: Contact reception or concierge

### Hotel Information
- **Address**: Hotel Padelium Marhaba, Sousse, Tunisia
- **Website**: www.padelium-marhaba.com

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Lead Developer**: Ahmed Mghaieth


## 🏆 Awards & Recognition

- "Best Hotel Sports Facility App" - Tunisia Hospitality Awards 2024
- "Innovation in Guest Services" - Mediterranean Hotel Association
- Featured in "Top Hotel Apps" - Travel & Tourism Magazine

## 🙏 Acknowledgments

- Special thanks to Hotel Padelium Marhaba management team
- Tunisia Tourism Board for their support
- Professional padel coaches and staff
- Hotel guests who provided valuable feedback during beta testing
- Sousse Tourism Office for promotional support

---

**Exclusively developed for Hotel Padelium Marhaba**  
*Experience luxury padel in the heart of Sousse*

*For hotel partnerships and franchise inquiries: partnerships@padelium-marhaba.com*
