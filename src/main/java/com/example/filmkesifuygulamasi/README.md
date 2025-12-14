# CineTrack - Film Keşif Uygulaması 🎬

Bu proje, TMDB API kullanarak güncel filmleri listeleyen, arama yapmaya olanak sağlayan ve film detaylarını gösteren modern bir Android uygulamasıdır. Mobil Geliştirici Challenge kapsamında geliştirilmiştir.

## ✨ Özellikler
- **4 Farklı Film Grubu:** Vizyondakiler, Popüler, En Çok Oy Alanlar ve Yakında Gelecekler.
- **Gelişmiş Arama:** Film adına göre gerçek zamanlı arama ve sonuç bulunamadığında uyarı.
- **Film Detayları:** Film afişi, puanı, yayın tarihi, türleri ve özeti.
- **Kullanıcı Dostu UI:** Jetpack Compose ile modern, hızlı ve karanlık mod uyumlu arayüz.
- **Hata Yönetimi:** İnternet bağlantısı kontrolleri ve kullanıcı bilgilendirme.

## 🛠 Kullanılan Teknolojiler ve Kütüphaneler
- **Kotlin:** Modern ve güvenli programlama dili.
- **Jetpack Compose:** Bildirimsel (declarative) UI bileşenleri.
- **Retrofit & Gson:** REST API entegrasyonu ve JSON veri işleme.
- **Coil:** Asenkron resim yükleme ve önbelleğe alma.
- **Navigation Compose:** Ekranlar arası akıcı geçiş yönetimi.

## 🏛 Mimari
Uygulamada **Modern Android Development (MAD)** prensipleri benimsenmiştir. UI ve iş mantığı Jetpack Compose'un modern state yönetimi ile kurgulanmıştır.

## 🔑 Kurulum
1. Bu depoyu klonlayın.
2. `MainActivity.kt` dosyasındaki `API_KEY` değişkenine TMDB API anahtarınızı ekleyin.
3. Projeyi Android Studio ile derleyip çalıştırın.