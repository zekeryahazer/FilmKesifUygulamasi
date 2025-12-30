# 🎬 CineTrack - Modern Film Keşif Deneyimi

Selamlar! 👋 Ben Zekerya.

Bu proje, **Mobil Geliştirici Challenge** kapsamında geliştirdiğim; modern Android teknolojilerini kullanarak sinema dünyasını cebinize getiren bir uygulamadır. Klasik bir ödevden ziyade, gerçek bir kullanıcının keyif alacağı akıcı bir deneyim tasarlamayı hedefledim.

## 🚀 Neler Yapabiliyor?
Uygulamayı geliştirirken kullanıcı deneyimini (UX) ön planda tuttum:

* **Keşfet:** "Ne izlesem?" derdine son. Vizyondakiler, Popülerler, En Çok Puan Alanlar ve Yakında Gelecekler tek ekranda.
* **Akıllı Arama:** Sadece film adını yazmanız yeterli. Aradığınız film yoksa sizi boş bir ekranla değil, bilgilendirici bir uyarıyla karşılıyor.
* **Detaylı Bakış:** Film afişlerinden özetine, yayın tarihinden türlerine kadar her detay elinizin altında.
* **Karanlık Mod:** OLED dostu, göz yormayan modern ve şık arayüz.

## 🛠 Kaputun Altında Neler Var? (Teknolojiler)
Bu projede "eski usul" (XML) yerine Android'in geleceği olan teknolojileri tercih ettim:

* **Kotlin:** Güvenli ve modern kod yapısı için.
* **Jetpack Compose:** Arayüzü çok daha hızlı ve esnek tasarlamak için (View sistemi yerine tamamen Compose kullandım).
* **Retrofit & Gson:** TMDB API ile konuşmak ve gelen verileri işlemek için.
* **Coil:** Film afişlerini internetten asenkron ve hızlı bir şekilde yüklemek için.
* **Navigation Compose:** Sayfalar arasında (Ana Sayfa -> Detay -> Profil) pürüzsüz geçişler sağlamak için.

## 🏛 Mimari Kararlarım
Spagetti koddan kaçınmak için projeyi modüler ve temiz tutmaya çalıştım. **State Hoisting** ve **Unidirectional Data Flow** (Tek Yönlü Veri Akışı) prensiplerine sadık kalarak, uygulamanın her durumda (internet kesintisi, hatalı arama vb.) kararlı çalışmasını sağladım.

## 🏃‍♂️ Nasıl Çalıştırırsınız?
1.  Projeyi bilgisayarınıza klonlayın (veya indirin).
2.  `MainActivity.kt` dosyasını açın.
3.  `API_KEY` kısmına kendi TMDB anahtarınızı yapıştırın (veya mevcut olanı kullanın).
4.  Android Studio'da "Run" tuşuna basın ve keyfini çıkarın! 🍿

---
*Geri bildirimleriniz benim için değerli. İncelediğiniz için teşekkürler!*