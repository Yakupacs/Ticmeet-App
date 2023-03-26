# Classes

## User
```
class User
{
    var userName : String?
    var userEmail : String?
    var userPassword : String?
    var userImage : UIImage?
    var userTopImage : UIImage?
    var userAge : Int?
    var userGender : String?
    var userBio : String?
    var userLocation : String?
    var userFollowers : [String]?
    var userFollowing : [String]?
    var userRegisterDate : Date?
    var userEventsID : [String]?
}
```

## Event
```
class Event
{
    var eventID = String()
    var eventCategory = String()
    var eventName = String()
    var eventSubscriber = Int()
    var eventDetail = String()
    var eventImage = String()
    var eventLocation = String()
    var eventLongitude = Double()
    var eventLatitude = Double()
    var eventComments = [String]()
    var eventAttented = Int()
    var eventUsersEmail = [String]()
    var eventCurrentUserIsAttented = Bool()
}
```

# Screens

- Kayıt Ol
- Kayıt Ol 2 (Email ve şifre ile kayıt olduktan sonra çıkacak ikinci kayıt olma sayfası. Burada kullanıcı adı, ad soyad, doğum tarihi, cinsiyet gibi kişisel bilgiler kaydedilecek.)
- Giriş Yap
- Şifremi Unuttum (Giriş Ekranında Şifremi Unuttum butonuna basıldığında açılacak.)
- Etkinlikler
- Etkinlik Detayı (Etkinlikler sayfası tıklandığında girilen etkinliğin detayları olacak.)
- Etkinlik Arama (Etkinlikler sayfasının en üstünde SearchBar şeklinde olacak.)
- Mesajlar
- Mesaj Detayı (Mesaj sayfasında mesaj tıklandığında açılacak detaylar.)
- Kullanıcı Profili
- Kullanıcı Profili Düzenle (Kullanıcı Profili sayfasında Profilimi Düzenle'ye tıklandığında açılacak.)

# Photos

<img width=201 src="https://user-images.githubusercontent.com/73075252/227617254-1c6a5148-9c2b-4800-8445-f3d1667214c2.png">
<img width=201 src="https://user-images.githubusercontent.com/73075252/227617260-f8747114-b25a-404c-be24-13beab919857.png">
<img width=201 src="https://user-images.githubusercontent.com/73075252/227617264-9c753d7c-1ef2-4e44-9db8-e4785cafba9a.png">
<img width=201 src="https://user-images.githubusercontent.com/73075252/227617266-a0895248-236b-4c67-b672-d4763962d827.png">
<img width=201 src="https://user-images.githubusercontent.com/73075252/227617269-a7b0a131-2bbf-4c20-8f52-1f3d5cd96fb7.png">
<img width=201 src="https://user-images.githubusercontent.com/73075252/227617272-4a5e4e2f-20fc-4ba4-993d-ac0d431b6081.png">
<img width=201 src="https://user-images.githubusercontent.com/73075252/227617278-c34bb28d-3686-41c8-98a4-ab969ab71fd4.png">
<img width=201 src="https://user-images.githubusercontent.com/73075252/227617280-6054618a-7a1e-4ebb-8b58-84f94adf7626.png">
<img width=201 src="https://user-images.githubusercontent.com/73075252/227617285-3b5e3e5b-7d2b-49d2-a0b5-3070d698a29a.png">
