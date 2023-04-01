//
//  userVC.swift
//  ticmeet
//
//  Created by Yakup on 19.03.2023.
//

import UIKit
import Firebase
import FirebaseFirestore

class userVC: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var logoutButton: UIButton!
    @IBOutlet weak var nameTopLabel: UILabel!
    @IBOutlet weak var topImageView: UIImageView!
    @IBOutlet weak var profileImageView: UIImageView!
    @IBOutlet weak var messageButton: UIButton!
    @IBOutlet weak var followOrSettingsButton: UIButton!
    @IBOutlet weak var nameProfileLabel: UILabel!
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var bioLabel: UILabel!
    @IBOutlet weak var followingCountLabel: UILabel!
    @IBOutlet weak var followersCountLabel: UILabel!
    @IBOutlet weak var attentedEventCountLabel: UILabel!
    @IBOutlet weak var eventCommentCountLabel: UILabel!
    @IBOutlet weak var userEventCollectionView: UICollectionView!
    @IBOutlet weak var backButton: UIButton!
    
    var gettingUser : User?
    var events = [Event]()
    var selectedEvent = Event()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        userEventCollectionView.delegate = self
        userEventCollectionView.dataSource = self
        fixAppearance()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        if gettingUser?.userEmail! == Auth.auth().currentUser?.email!{
            // Listeden kendi profiline tıklayıp girmiş.
            print("Listeden kendi profiline tıklayıp girmiş.")
            messageButton.isHidden = true
            backButton.isHidden = false
            backButton.isEnabled = true
            logoutButton.isHidden = false
            followOrSettingsButton.setTitle("Düzenle", for: .normal)
            getData(userEmail: (Auth.auth().currentUser?.email)!)
        }
        else if gettingUser == nil{
            // Tabbardan kendi profiline tıklamış.
            print("Tabbardan kendi profiline tıklamış.")
            messageButton.isHidden = true
            backButton.isHidden = true
            backButton.isEnabled = false
            logoutButton.isHidden = false
            followOrSettingsButton.setTitle("Düzenle", for: .normal)
            getData(userEmail: (Auth.auth().currentUser?.email)!)
        }
        else{
            // Listeden başkasının profiline tıklamış.
            print("Listeden başkasının profiline tıklamış.")
            messageButton.isHidden = false
            backButton.isHidden = false
            backButton.isEnabled = true
            logoutButton.isHidden = true
            getData(userEmail: gettingUser!.userEmail!)
            if gettingUser?.userFollowers?.contains((Auth.auth().currentUser?.email)!) == true{
                followOrSettingsButton.setTitle("Takiptesin", for: .normal)
            }
            else{
                followOrSettingsButton.setTitle("Takip et", for: .normal)
            }
        }
        getEvents()
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if let cell = userEventCollectionView.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath) as? userEventsCell{
            
            cell.eventImageView.sd_setImage(with: URL(string: events[indexPath.row].eventImage))
            cell.eventImageView.layer.cornerRadius = 30
            return cell
            
        }
        return UICollectionViewCell()
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        selectedEvent = events[indexPath.row]
        performSegue(withIdentifier: "toDetail", sender: nil)
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return events.count
    }
    
    @IBAction func followOrProfileFunc(_ sender: Any) {
        if followOrSettingsButton.titleLabel?.text == "Düzenle"{
            // Profili düzenle
            print("Profili düzenle")
            performSegue(withIdentifier: "toChange", sender: nil)
        }
        else if followOrSettingsButton.titleLabel?.text == "Takip et"{
            // Kullanıcıyı takip et
            print("Kullanıcıyı takip et")
            gettingUser!.userFollowers?.append((Auth.auth().currentUser?.email)!)
            followUser(addUser: gettingUser!)
            followOrSettingsButton.setTitle("Takiptesin", for: .normal)
            getData(userEmail: gettingUser!.userEmail!)
            
            var count = Int(followersCountLabel.text!)!
            count += 1
            followersCountLabel.text = "\(count)"
        }
        else if followOrSettingsButton.titleLabel?.text == "Takiptesin"{
            // Kullanıcıyı takipten bırak
            print("Kullanıcıyı takipten bırak")
            gettingUser!.userFollowers = gettingUser!.userFollowers!.filter { $0 != Auth.auth().currentUser?.email }
            unfollowUser(selectedUser: gettingUser!)
            followOrSettingsButton.setTitle("Takip et", for: .normal)
            getData(userEmail: gettingUser!.userEmail!)

            var count = Int(followersCountLabel.text!)!
            count -= 1
            followersCountLabel.text = "\(count)"
        }
    }
    
    @IBAction func backButton(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    @IBAction func messageFunc(_ sender: Any) {
        performSegue(withIdentifier: "toMessage", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "toDetail"{
            let destVC = segue.destination as! eventDetailVC
            destVC.getEvent = selectedEvent
        }
        else if segue.identifier == "toChange"{
            let destVC = segue.destination as! profileEditVC
            destVC.gettingUser = gettingUser!
        }
        else if segue.identifier == "toMessage"{
            let destVC = segue.destination as! messageVC
            destVC.otherUser = gettingUser!
        }
    }
    
    func getData(userEmail: String){
        let fireStoreDatabase = Firestore.firestore()
        
        fireStoreDatabase.collection("User").whereField("userEmail", isEqualTo: userEmail).getDocuments { querySnapshot, error in
            if let error = error {
                print("Veri alınamadı. \(error.localizedDescription)")
            }
            else{
                guard let querySnapshot = querySnapshot else{
                    print("Veri alınamadı: Snapshot yok")
                    return
                }
                
                for document in querySnapshot.documents{
                    
                    if let userName = document.get("userName") as? String{
                        self.nameTopLabel.text = userName
                        self.nameProfileLabel.text = userName
                        self.gettingUser?.userName = userName
                    }
                    if let userUsername = document.get("userUsername") as? String{
                        self.usernameLabel.text = "@\(userUsername)"
                        self.gettingUser?.userUsername = userUsername
                    }
                    if let userBio = document.get("userBio") as? String{
                        self.bioLabel.text = userBio
                        self.gettingUser?.userBio = userBio
                    }
                    if let userGender = document.get("userGender") as? String{
                        self.gettingUser?.userGender = userGender
                    }
                    if let userCity = document.get("userLocation") as? String{
                        self.gettingUser?.userLocation = userCity
                    }
                    if let userFollowers = document.get("userFollowers") as? [String]{
                        self.followersCountLabel.text = String(userFollowers.count)
                        self.gettingUser?.userFollowers = userFollowers
                    }
                    if let userFollowing = document.get("userFollowing") as? [String]{
                        self.followingCountLabel.text = String(userFollowing.count)
                        self.gettingUser?.userFollowing = userFollowing
                    }
                    if let userImage = document.get("userImage") as? String{
                        self.profileImageView.sd_setImage(with: URL(string: userImage))
                        self.gettingUser?.userImage = userImage
                    }
                    if let userTopImage = document.get("userTopImage") as? String{
                        self.topImageView.sd_setImage(with: URL(string: userTopImage))
                        self.gettingUser?.userTopImage = userTopImage
                    }
                    if let userEventsID = document.get("userEventsID") as? [String]{
                        self.gettingUser?.userEventsID = userEventsID
                        self.attentedEventCountLabel.text = "\(userEventsID.count)"
                        self.getEvents()
                    }
                    if let userAge = document.get("userAge") as? Int{
                        self.gettingUser?.userAge = userAge
                    }
                }
                self.userEventCollectionView.reloadData()
            }
        }
    }
    
    func getEvents(){
        let fireStoreDatabase = Firestore.firestore()
        
        fireStoreDatabase.collection("Event").addSnapshotListener { snapshot, error in
            if error != nil{
                print(error!.localizedDescription)
            }
            else{
                if snapshot?.isEmpty != true && snapshot != nil{
                    
                    self.events = []
                    
                    for document in snapshot!.documents{
                        
                        if let eventID = document.get("eventID") as? String{

                            if self.gettingUser?.userEventsID == nil{
                                return
                            }
                            
                            for u in self.gettingUser!.userEventsID!{
                                if u == eventID{
                                    let oneEvent = Event()
                                    
                                    if let eventID = document.get("eventID") as? String{
                                        oneEvent.eventID = eventID
                                    }
                                    if let eventCategory = document.get("eventCategory") as? String{
                                        oneEvent.eventCategory = eventCategory
                                    }
                                    if let eventImage = document.get("eventImage") as? String{
                                        oneEvent.eventImage = eventImage
                                    }
                                    if let eventAttented = document.get("eventAttented") as? Int{
                                        oneEvent.eventAttented = eventAttented
                                    }
                                    if let eventName = document.get("eventName") as? String{
                                        oneEvent.eventName = eventName
                                    }
                                    if let eventDetail = document.get("eventDetail") as? String{
                                        oneEvent.eventDetail = eventDetail
                                    }
                                    if let eventLatitude = document.get("eventLatitude") as? Double{
                                        oneEvent.eventLatitude = eventLatitude
                                    }
                                    if let eventLongitude = document.get("eventLongitude") as? Double{
                                        oneEvent.eventLongitude = eventLongitude
                                    }
                                    if let eventLocation = document.get("eventLocation") as? String{
                                        oneEvent.eventLocation = eventLocation
                                    }
                                    if let eventUsersEmail = document.get("eventUsersEmail") as? [String]{
                                        oneEvent.eventUsersEmail = eventUsersEmail
                                    }
                                    
                                    self.events.append(oneEvent)
                                }
                            }
                        }
                    }
                    self.userEventCollectionView.reloadData()
                }
            }
        }
    }
    
    @IBAction func logoutFunc(_ sender: Any) {
        do{
            try Auth.auth().signOut()
            performSegue(withIdentifier: "toExit", sender: nil)
        }catch{
            print(error.localizedDescription)
        }
    }
    
    func followUser(addUser: User){
        let firestoreDatabase = Firestore.firestore()
        
        firestoreDatabase.collection("User")
            .whereField("userEmail", isEqualTo: (Auth.auth().currentUser?.email)!)
            .getDocuments { (querySnapshot, error) in
                if let error = error {
                    print("Veri alınamadı: \(error.localizedDescription)")
                } else {
                    guard let querySnapshot = querySnapshot else {
                        print("Veri alınamadı: Snapshot yok")
                        return
                    }
                    
                    for document in querySnapshot.documents {
                        var userFollowing = document.get("userFollowing") as? [String] ?? []
                        
                        if let currentUserEmail = addUser.userEmail {
                            userFollowing.append(currentUserEmail)
                        }
                        
                        // Firestore veri güncelleme işlemi
                        firestoreDatabase.collection("User").document(document.documentID)
                            .setData(["userFollowing": userFollowing], merge: true) { error in
                                if let error = error {
                                    print("Veri güncelleme işlemi başarısız: \(error.localizedDescription)")
                                } else {
                                    print("Veri güncelleme işlemi başarılı.")
                                }
                            }
                    }
                }
            }
        
        firestoreDatabase.collection("User").whereField("userEmail", isEqualTo: addUser.userEmail!).getDocuments { querySnapshot, error in
            if let error = error {
                print("Veri alınamadı: \(error.localizedDescription)")
            }
            else{
                guard let querySnapshot = querySnapshot else{
                    print("Veri alınamadı: Snapshot yok")
                    return
                }
                
                for document in querySnapshot.documents{
                    var userFollowers = document.get("userFollowers") as? [String] ?? []
                    
                    // Güncelleme işlemini burada gerçekleştirin
                    userFollowers.append((Auth.auth().currentUser?.email)!)
                    
                    // Firestore veri güncelleme işlemi
                    firestoreDatabase.collection("User").document(document.documentID)
                        .setData(["userFollowers": userFollowers], merge: true) { error in
                            if let error = error {
                                print("Veri güncelleme işlemi başarısız: \(error.localizedDescription)")
                            } else {
                                print("Veri güncelleme işlemi başarılı.")
                            }
                        }
                }
            }
        }
        
    }
    
    func unfollowUser(selectedUser: User){
        let firestoreDatabase = Firestore.firestore()
        
        firestoreDatabase.collection("User")
            .whereField("userEmail", isEqualTo: (Auth.auth().currentUser?.email)!)
            .getDocuments { (querySnapshot, error) in
                if let error = error {
                    print("Veri alınamadı: \(error.localizedDescription)")
                } else {
                    guard let querySnapshot = querySnapshot else {
                        print("Veri alınamadı: Snapshot yok")
                        return
                    }
                    
                    for document in querySnapshot.documents {
                        var userFollowing = document.get("userFollowing") as? [String] ?? []
                        
                        
                        if let currentUserEmail = selectedUser.userEmail,
                           let index = userFollowing.firstIndex(of: currentUserEmail) {
                            userFollowing.remove(at: index)
                        }
                        
                        // Firestore veri güncelleme işlemi
                        firestoreDatabase.collection("User").document(document.documentID)
                            .setData(["userFollowing": userFollowing], merge: true) { error in
                                if let error = error {
                                    print("Veri güncelleme işlemi başarısız: \(error.localizedDescription)")
                                } else {
                                    print("Veri güncelleme işlemi başarılı.")
                                }
                            }
                    }
                }
            }
        
        firestoreDatabase.collection("User")
            .whereField("userEmail", isEqualTo: selectedUser.userEmail!)
            .getDocuments { (querySnapshot, error) in
                if let error = error {
                    print("Veri alınamadı: \(error.localizedDescription)")
                } else {
                    guard let querySnapshot = querySnapshot else {
                        print("Veri alınamadı: Snapshot yok")
                        return
                    }
                    
                    for document in querySnapshot.documents {
                        var userFollowers = document.get("userFollowers") as? [String] ?? []
                        
                        
                        if let currentUserEmail = Auth.auth().currentUser?.email!,
                           let index = userFollowers.firstIndex(of: currentUserEmail) {
                            userFollowers.remove(at: index)
                        }
                        
                        // Firestore veri güncelleme işlemi
                        firestoreDatabase.collection("User").document(document.documentID)
                            .setData(["userFollowers": userFollowers], merge: true) { error in
                                if let error = error {
                                    print("Veri güncelleme işlemi başarısız: \(error.localizedDescription)")
                                } else {
                                    print("Veri güncelleme işlemi başarılı.")
                                }
                            }
                    }
                }
            }
    }
    
    func fixAppearance(){
        profileImageView.layer.cornerRadius = 45
        profileImageView.layer.borderWidth = 2
        profileImageView.layer.borderColor = UIColor.white.cgColor
    }
}
