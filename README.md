# 🎬 CineTrack - Film Keşif Uygulaması

CineTrack, **TMDB API** kullanarak güncel film verilerini modern bir arayüzle sunan, **Android (Kotlin) & Jetpack Compose** ile geliştirilmiş bir film keşif uygulamasıdır.

## 📱 Ekran Görüntüleri

| Ana Ekran | Arama Ekranı | Film Detayı |
|:---:|:---:|:---:|
| ![Home](screenshots/home.jpg) | ![Search](screenshots/search.jpg) | ![Detail](screenshots/detail.jpg) |

*(Not: Proje klasörünüze `screenshots` adında bir klasör açıp, aldığınız ekran görüntülerini belirtilen isimlerle içine atınız.)*

## ✨ Özellikler

Uygulama, kullanıcıların filmleri kolayca keşfetmesini sağlayan aşağıdaki özelliklere sahiptir:

* **🏠 Ana Sayfa Vitrini:** Vizyondakiler, Popüler, En Çok Oy Alanlar ve Yakında Gelecekler kategorilerinde yatay kaydırılabilir listeler.
* **🔍 Akıllı Arama:** Film ismine göre dinamik arama yapabilme (En az 3 karakter ile otomatik sorgu).
* **📄 Detay Sayfası:** Seçilen filmin dev afişi, özeti, puanı, yayın tarihi ve tür bilgilerinin şık tasarımı.
* **🎨 Modern Arayüz:** Göz yormayan **Koyu Tema (Dark Mode)** ve Material Design 3 bileşenleri.
* **⚡ Hızlı ve Akıcı:** Jetpack Compose ile geliştirilmiş yüksek performanslı UI.

## 🛠️ Teknoloji Yığını

Bu proje, modern Android geliştirme araçları kullanılarak oluşturulmuştur:

* **Dil:** [Kotlin](https://kotlinlang.org/)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetbrains/compose) (Material3)
* **Navigasyon:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) (Tek Activity, çoklu ekran yönetimi)
* **Ağ (Network):** [Retrofit](https://square.github.io/retrofit/) & Gson (REST API istekleri için)
* **Görsel Yükleme:** [Coil](https://coil-kt.github.io/coil/compose/) (Asenkron resim yükleme)
* **Asenkron İşlemler:** Coroutines & LaunchedEffect

## 🚀 Kurulum

1.  Bu depoyu (repository) klonlayın.
2.  Android Studio'da projeyi açın.
3.  İnternet bağlantınızın olduğundan emin olun (TMDB API verileri için gereklidir).
4.  Projeyi derleyin ve çalıştırın! 🎥

---
*Geliştirici: Zekerya Hazer*
