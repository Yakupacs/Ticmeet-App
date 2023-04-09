//
//  eventDetailVC.swift
//  ticmeet
//
//  Created by Yakup on 23.03.2023.
//

import UIKit
import Firebase
import SDWebImage
import MapKit

class eventDetailVC: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, MKMapViewDelegate {
    
    @IBOutlet weak var eventNameLabel: UILabel!
    @IBOutlet weak var usersCollectionView: UICollectionView!
    @IBOutlet weak var eventLocationLabel: UILabel!
    @IBOutlet weak var eventAttentedLabel: UILabel!
    @IBOutlet weak var eventDetailLabel: UILabel!
    @IBOutlet weak var eventImageView: UIImageView!
    
    @IBOutlet weak var commentButton: UIButton!
    @IBOutlet weak var saveRemoveButton: UIButton!
    @IBOutlet weak var attentedView: UIView!
    @IBOutlet weak var locationView: UIView!
    @IBOutlet weak var detailView: UIView!
    @IBOutlet weak var eventUsersView: UIView!
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var commentCollectionView: UICollectionView!
    
    var getEvent = Event()
    var eventUsers = [User]()
    var comments = [Comment]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        usersCollectionView.delegate = self
        usersCollectionView.dataSource = self
        commentCollectionView.delegate = self
        commentCollectionView.dataSource = self
        
        setConfigure()
        
        let layout = usersCollectionView.collectionViewLayout as? UICollectionViewFlowLayout
        layout?.minimumLineSpacing = -5
        
        if getEvent.eventUsersEmail.contains((Auth.auth().currentUser?.email)!) == true{
            saveRemoveButton.setImage(UIImage(named: "checked32x32"), for: .normal)
        }
        else{
            saveRemoveButton.setImage(UIImage(named: "add32x32"), for: .normal)
        }
        
        addAnnotationMapView()
    }
    override func viewWillAppear(_ animated: Bool) {
        getEventComments()
        canCommentCheck()
    }
    
    func canCommentCheck(){
        commentButton.isEnabled = true
        let firestore = Firestore.firestore()
        
        firestore.collection("Comment").whereField("commentEventID", isEqualTo: getEvent.eventID).getDocuments { querySnapshot, error in
            if let error = error{
                print("Hata: \(error.localizedDescription)")
            }
            else{
                guard let querySnapshot = querySnapshot else { return }
                
                for document in querySnapshot.documents{
                                        
                    if let commentEmail = document.get("commentEmail") as? String{
                        
                        if commentEmail == Auth.auth().currentUser?.email!{
                            self.commentButton.isEnabled = false
                        }
                        
                    }
                    
                }
            }
        }
    }
    
    @IBAction func commentFunc(_ sender: Any) {
        performSegue(withIdentifier: "toComment", sender: nil)
    }
    
    func addAnnotationMapView(){
        let location = CLLocationCoordinate2D(latitude: getEvent.eventLatitude, longitude: getEvent.eventLongitude)
        
        let span = MKCoordinateSpan(latitudeDelta: 0.01, longitudeDelta: 0.01)
        let region = MKCoordinateRegion(center: location, span: span)
        
        mapView.setRegion(region, animated: true)
        
        let annotation = MKPointAnnotation()
        
        annotation.coordinate = location
        annotation.title = getEvent.eventLocation
        
        mapView.addAnnotation(annotation)
    }
    
    @IBAction func addOrRemoveFunc(_ sender: Any) {
        if getEvent.eventUsersEmail.contains((Auth.auth().currentUser?.email)!) == true{
            saveRemoveButton.setImage(UIImage(named: "add32x32"), for: .normal)
            removeEventUser(selectedEvent: getEvent)
            removeUsersEventID(eventID: getEvent.eventID)
            getEvent.eventUsersEmail = getEvent.eventUsersEmail.filter { $0 != Auth.auth().currentUser?.email }
        }
        else{
            saveRemoveButton.setImage(UIImage(named: "checked32x32"), for: .normal)
            addEventUser(selectedEvent: getEvent)
            addUsersEventID(eventID: getEvent.eventID)
            getEvent.eventUsersEmail.append((Auth.auth().currentUser?.email)!)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == commentCollectionView{
            if let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cellComment", for: indexPath) as? commentCell{
                cell.userUsernameLabel.text = "@\(comments[indexPath.row].commentUserUsername!)"
                cell.userNameLabel.text = comments[indexPath.row].commentUserName
                cell.userCommentLabel.text = comments[indexPath.row].comment
                cell.layer.cornerRadius = 20
                return cell
            }
        }
        else{
            if let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cellUser", for: indexPath) as? eventUsersCell{
                cell.userImageView.sd_setImage(with: URL(string: eventUsers[indexPath.row].userImage!))
                cell.userImageView.layer.cornerRadius = 20
                cell.userImageView.layer.borderWidth = 1
                cell.userImageView.layer.borderColor = UIColor.black.cgColor
                return cell
            }
        }

        
        return UICollectionViewCell()
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == commentCollectionView{
            return comments.count
        }
        else{
            return eventUsers.count
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == usersCollectionView{
            performSegue(withIdentifier: "toUserDetail", sender: nil)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "toUserDetail"{
            let destVC = segue.destination as! eventUserListVC
            destVC.users = eventUsers
        }
        else if segue.identifier == "toComment"{
            let destVC = segue.destination as! eventCommentVC
            destVC.selectedEvent = getEvent
        }
    }
    
    func getEventComments(){
        let firestore = Firestore.firestore()
        
        firestore.collection("Comment").getDocuments { querySnapshot, error in
            if let error = error{
                print("Hata: \(error.localizedDescription)")
            }
            else{
                guard let querySnapshot = querySnapshot else { return }
                
                self.comments = []
                
                for document in querySnapshot.documents{
                    if let commentEventID = document.get("commentEventID") as? String{
                                                
                        if commentEventID == self.getEvent.eventID{
                                                        
                            let comment = document.get("comment") as? String
                            let commentUserName = document.get("commentUserName") as? String
                            let commentUserUsername = document.get("commentUserUsername") as? String
                                                        
                            self.comments.append(Comment(comment: comment, commentUserName: commentUserName, commentUserUsername: commentUserUsername))
                        }
                        
                    }

                }
                
                self.commentCollectionView.reloadData()
            }
        }
    }
    
    func getUsersEmail(){
        let emails = getEvent.eventUsersEmail
        
        let firestore = Firestore.firestore()
        
        if emails.isEmpty == true{
            return
        }
        
        firestore.collection("User").whereField("userEmail", in: emails).getDocuments { (snapshot, error) in
            if let error = error {
                print("Hata: \(error.localizedDescription)")
            } else {
                guard let snapshot = snapshot else { return }
                self.eventUsers = []
                
                for document in snapshot.documents {
                    let usersEmails = document.get("userEmail") as? String
                    let usersUsername = document.get("userUsername") as? String
                    let usersName = document.get("userName") as? String
                    let usersImage = document.get("userImage") as? String
                    let userTopImage = document.get("userTopImage") as? String
                    let userAge = document.get("userAge") as? Int
                    let usersGender = document.get("userGender") as? String
                    let usersBio = document.get("userBio") as? String
                    let usersLocation = document.get("userLocation") as? String
                    let usersFollowers = document.get("userFollowers") as? [String]
                    let usersFollowing = document.get("userFollowing") as? [String]
                    let userEventsID = document.get("userEventsID") as? [String]
                    
                    let user = User(userName: usersName,userUsername: usersUsername, userEmail: usersEmails, userPassword: nil, userImage: usersImage, userTopImage: userTopImage, userAge: userAge, userGender: usersGender, userBio: usersBio, userLocation: usersLocation, userFollowers: usersFollowers, userFollowing: usersFollowing, userRegisterDate: nil, userEventsID: userEventsID)
                    self.eventUsers.append(user)
                }
                
                self.usersCollectionView.reloadData()
            }
        }
        
    }
    
    func setConfigure(){
        eventNameLabel.text = getEvent.eventName
        eventLocationLabel.text = getEvent.eventLocation
        eventDetailLabel.text = getEvent.eventDetail
        eventAttentedLabel.text = "\(getEvent.eventAttented)"
        eventImageView.sd_setImage(with: URL(string: getEvent.eventImage))
        
        getUsersEmail()
        
        detailView.layer.cornerRadius = 15
        attentedView.layer.cornerRadius = 15
        locationView.layer.cornerRadius = 15
        eventUsersView.layer.cornerRadius = 15
        mapView.layer.cornerRadius = 20
    }
    
    @IBAction func backFunc(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    // MARK: - addUsersEventID
    func addUsersEventID(eventID: String){
        guard let currentUserEmail = Auth.auth().currentUser?.email else { return }
        
        let db = Firestore.firestore()
        let usersRef = db.collection("User")
        
        usersRef.getDocuments { (querySnapshot, error) in
            if let error = error {
                print("Error getting users: \(error)")
            } else {
                guard let querySnapshot = querySnapshot else { return }
                for document in querySnapshot.documents {
                    let userData = document.data()
                    if let userEmail = userData["userEmail"] as? String, userEmail == currentUserEmail {
                        var userEventsID = userData["userEventsID"] as? [String] ?? []
                        userEventsID.append(eventID)
                        
                        let documentRef = usersRef.document(document.documentID)
                        documentRef.updateData(["userEventsID": userEventsID]) { (error) in
                            if let error = error {
                                print("Error updating user data: \(error)")
                            } else {
                                print("Event added to user successfully")
                            }
                        }
                    }
                }
            }
        }
    }
    
    // MARK: - removeUsersEventID
    func removeUsersEventID(eventID: String){
        guard let currentUserEmail = Auth.auth().currentUser?.email else { return }
            
            let db = Firestore.firestore()
            let usersRef = db.collection("User")
            
            usersRef.getDocuments { (querySnapshot, error) in
                if let error = error {
                    print("Error getting users: \(error)")
                } else {
                    guard let querySnapshot = querySnapshot else { return }
                    for document in querySnapshot.documents {
                        let userData = document.data()
                        if let userEmail = userData["userEmail"] as? String, userEmail == currentUserEmail {
                            var userEventsID = userData["userEventsID"] as? [String] ?? []
                            
                            if let index = userEventsID.firstIndex(of: eventID) {
                                userEventsID.remove(at: index)
                                
                                let documentRef = usersRef.document(document.documentID)
                                documentRef.updateData(["userEventsID": userEventsID]) { (error) in
                                    if let error = error {
                                        print("Error updating user data: \(error)")
                                    } else {
                                        print("Event removed from user successfully")
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }
    
    
    // MARK: - removeEventUser
    func removeEventUser(selectedEvent: Event){
        let firestoreDatabase = Firestore.firestore()
        
        firestoreDatabase.collection("Event")
            .whereField("eventName", isEqualTo: selectedEvent.eventName)
            .getDocuments { (querySnapshot, error) in
                if let error = error {
                    print("Veri alınamadı: \(error.localizedDescription)")
                } else {
                    guard let querySnapshot = querySnapshot else {
                        print("Veri alınamadı: Snapshot yok")
                        return
                    }
                    
                    for document in querySnapshot.documents {
                        var eventUsersEmails = document.get("eventUsersEmail") as? [String] ?? []
                        var eventAttented = document.get("eventAttented") as? Int ?? 0
                        
                        // Güncelleme işlemini burada gerçekleştirin
                        eventAttented -= 1
                        if let currentUserEmail = Auth.auth().currentUser?.email,
                           let index = eventUsersEmails.firstIndex(of: currentUserEmail) {
                            eventUsersEmails.remove(at: index)
                        }
                        
                        // Firestore veri güncelleme işlemi
                        firestoreDatabase.collection("Event").document(document.documentID)
                            .setData(["eventUsersEmail": eventUsersEmails,
                                      "eventAttented": eventAttented], merge: true) { error in
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
    
    // MARK: - addEventUser
    func addEventUser(selectedEvent: Event){
        let firestoreDatabase = Firestore.firestore()
        
        firestoreDatabase.collection("Event")
            .whereField("eventName", isEqualTo: selectedEvent.eventName)
            .getDocuments { (querySnapshot, error) in
                if let error = error {
                    print("Veri alınamadı: \(error.localizedDescription)")
                } else {
                    guard let querySnapshot = querySnapshot else {
                        print("Veri alınamadı: Snapshot yok")
                        return
                    }
                    
                    for document in querySnapshot.documents {
                        var eventUsersEmails = document.get("eventUsersEmail") as? [String] ?? []
                        var eventAttented = document.get("eventAttented") as? Int ?? 0
                        
                        // Güncelleme işlemini burada gerçekleştirin
                        eventAttented += 1
                        if let currentUserEmail = Auth.auth().currentUser?.email {
                            eventUsersEmails.append(currentUserEmail)
                        }
                        
                        // Firestore veri güncelleme işlemi
                        firestoreDatabase.collection("Event").document(document.documentID)
                            .setData(["eventUsersEmail": eventUsersEmails,
                                      "eventAttented": eventAttented], merge: true) { error in
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
}
