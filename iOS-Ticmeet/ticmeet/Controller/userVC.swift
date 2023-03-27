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
    
    var user = User()
    var events = [Event]()
    var selectedEvent = Event()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        userEventCollectionView.delegate = self
        userEventCollectionView.dataSource = self
        fixAppearance()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        getData()
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
    
    @IBAction func toChange(_ sender: Any) {
        performSegue(withIdentifier: "toChange", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "toDetail"{
            let destVC = segue.destination as! eventDetailVC
            destVC.getEvent = selectedEvent
        }
    }
    
    func getData(){
        let fireStoreDatabase = Firestore.firestore()
        
        fireStoreDatabase.collection("User").whereField("userEmail", isEqualTo: (Auth.auth().currentUser?.email)!).getDocuments { querySnapshot, error in
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
                    }
                    if let userUsername = document.get("userUsername") as? String{
                        self.usernameLabel.text = "@\(userUsername)"
                    }
                    if let userBio = document.get("userBio") as? String{
                        self.bioLabel.text = userBio
                    }
                    if let userFollowers = document.get("userFollowers") as? [String]{
                        self.followersCountLabel.text = String(userFollowers.count)
                    }
                    if let userFollowing = document.get("userFollowing") as? [String]{
                        self.followingCountLabel.text = String(userFollowing.count)
                    }
                    if let userImage = document.get("userImage") as? String{
                        self.profileImageView.sd_setImage(with: URL(string: userImage))
                    }
                    if let userTopImage = document.get("userTopImage") as? String{
                        self.topImageView.sd_setImage(with: URL(string: userTopImage))
                    }
                    if let userEventsID = document.get("userEventsID") as? [String]{
                        self.user.userEventsID = userEventsID
                        self.getEvents()
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

                            if self.user.userEventsID == nil{
                                return
                            }
                            
                            for u in self.user.userEventsID!{
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
    func fixAppearance(){
        profileImageView.layer.cornerRadius = 45
        profileImageView.layer.borderWidth = 2
        profileImageView.layer.borderColor = UIColor.white.cgColor
    }
}
