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
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        userEventCollectionView.delegate = self
        userEventCollectionView.dataSource = self
        fixAppearance()
        getData()
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if let cell = userEventCollectionView.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath) as? userEventsCell{
            
            cell.eventImageView.image = UIImage(named: "poster")
            cell.eventImageView.layer.cornerRadius = 20
            return cell
            
        }
        return UICollectionViewCell()
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 5
    }
    
    @IBAction func toChange(_ sender: Any) {
        performSegue(withIdentifier: "toChange", sender: nil)
    }
    
    func getData(){
        let fireStoreDatabase = Firestore.firestore()
        
        fireStoreDatabase.collection("User").addSnapshotListener { snapshot, error in
            if error != nil{
                print(error!.localizedDescription)
            }
            else{
                if snapshot?.isEmpty != true && snapshot != nil{
                    for document in snapshot!.documents{
                        
                        let currentUserEmail = Auth.auth().currentUser?.email
                        
                        if let userEmail = document.get("userEmail") as? String{
                            
                            if currentUserEmail == userEmail{
                                
                                let documentID = document.documentID
                                
                                if let userName = document.get("userName") as? String{
                                    self.nameTopLabel.text = userName
                                    self.nameProfileLabel.text = userName
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
                                
                            }
                            
                        }
                        
                    }
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
