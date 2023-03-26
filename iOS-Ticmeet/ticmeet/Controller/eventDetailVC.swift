//
//  eventDetailVC.swift
//  ticmeet
//
//  Created by Yakup on 23.03.2023.
//

import UIKit
import Firebase
import SDWebImage

class eventDetailVC: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var eventNameLabel: UILabel!
    @IBOutlet weak var usersCollectionView: UICollectionView!
    @IBOutlet weak var eventLocationLabel: UILabel!
    @IBOutlet weak var eventAttentedLabel: UILabel!
    @IBOutlet weak var eventDetailLabel: UILabel!
    @IBOutlet weak var eventImageView: UIImageView!
    
    @IBOutlet weak var attentedView: UIView!
    @IBOutlet weak var locationView: UIView!
    @IBOutlet weak var detailView: UIView!
    @IBOutlet weak var eventUsersView: UIView!
    
    var getEvent = Event()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        usersCollectionView.delegate = self
        usersCollectionView.dataSource = self
        setConfigure()
        
        let layout = usersCollectionView.collectionViewLayout as? UICollectionViewFlowLayout
        layout?.minimumLineSpacing = -5
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath) as? eventUsersCell{
            cell.userImageView.image = UIImage(named: "yakup")
            cell.userImageView.layer.cornerRadius = 20
            cell.userImageView.layer.borderWidth = 1
            cell.userImageView.layer.borderColor = UIColor.black.cgColor
            return cell
        }
        
        return UICollectionViewCell()
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 15
    }
    
    func getUsersEmail(){
//        var emails = getEvent.eventUsersEmail
//        var users = [User]()
//
//        let firestore = Firestore.firestore()
//
//        firestore.collection("User").whereField("userEmail", in: emails).getDocuments { (snapshot, error) in
//            if let error = error {
//                print("Hata: \(error.localizedDescription)")
//            } else {
//                guard let snapshot = snapshot else { return }
//                for document in snapshot.documents {
//                    var usersEmails = document.get("userEmail") as? String
//                    var usersName = document.get("userName") as? String
//                    var usersImage = document.get("userImage") as? String
//                    var userTopImage = document.get("userTopImage") as? String
//                    var userAge = document.get("userAge") as? Int
//                    var usersGender = document.get("userGender") as? String
//                    var usersBio = document.get("userBio") as? String
//                    var usersLocation = document.get("userLocation") as? String
//                    var usersFollowers = document.get("userFollowers") as? [String]
//                    var usersFollowing = document.get("userFollowing") as? [String]
//
//                    let user = User(userName: usersName, userEmail: usersEmails, userPassword: nil, userImage: UIImage(), userTopImage: nil, userAge: userAge, userGender: usersGender, userBio: usersBio, userLocation: usersLocation, userFollowers: usersFollowers, userFollowing: usersFollowing, userRegisterDate: nil, userEventsID: nil)
//                    users.append(user)
//                }
//                // Kullanıcıları aldıktan sonra kullanabilirsiniz.
//                print("Email'e sahip kullanıcılar: \(users)")
//            }
//        }
//
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
    }
    
    @IBAction func backFunc(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
}
