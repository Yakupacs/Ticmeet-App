//
//  eventsVC.swift
//  ticmeet
//
//  Created by Yakup on 18.03.2023.
//

import UIKit
import Firebase
import FirebaseFirestore
import SDWebImage
    
class eventsVC: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var konserCollectionView: UICollectionView!
    
    var user = User()
    var allEvents = [Event]()
    var selectedEvent = Event()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        getFirestoreDataFirebase()

        konserCollectionView.delegate = self
        konserCollectionView.dataSource = self
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return allEvents.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if let cell = konserCollectionView.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath) as? eventCell{
            cell.eventUserCountLabel.text = String(allEvents[indexPath.row].eventAttented)
            cell.eventImageView.sd_setImage(with: URL(string: allEvents[indexPath.row].eventImage))
            cell.eventImageView.layer.cornerRadius = 20
            cell.userCountView.layer.cornerRadius = 12
            cell.addButton.tag = indexPath.row
            cell.addButton.addTarget(self, action: #selector(addEvent), for: .touchUpInside)
            return cell
        }
        
        return UICollectionViewCell()
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        selectedEvent = allEvents[indexPath.row]
        performSegue(withIdentifier: "toDetail", sender: nil)
    }
    
    @objc func addEvent(){
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "toDetail"{
            let destVC = segue.destination as! eventDetailVC
            print("Gönderilecek etkinlik adı: \(selectedEvent.eventName)")
            print("Gönderilecek etkinlik lokasyonu: \(selectedEvent.eventLocation)")
            print("Gönderilecek etkinlik türü: \(selectedEvent.eventCategory)")
            destVC.getEvent = selectedEvent
        }
    }

    func getFirestoreDataFirebase(){
        print("FİREBASE VERİ ÇEKME İŞLEMİ")
        let fireStoreDatabase = Firestore.firestore()
        
        fireStoreDatabase.collection("Event").addSnapshotListener { snapshot, error in
            if error != nil{
                print(error!.localizedDescription)
            }
            else
            {
                if snapshot?.isEmpty != true && snapshot != nil{
                    
                    self.allEvents = []
                    
                    for document in snapshot!.documents{
            
                        let oneEvent = Event()
                        
                        let documentID = document.documentID

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
                        if let eventUsers = document.get("eventUsersID") as? [String]{
                            oneEvent.eventUsersID = eventUsers
                        }
                        self.allEvents.append(oneEvent)
                    }
                    self.konserCollectionView.reloadData()
                }
            }
        }
    
        // USER
        
//        fireStoreDatabase.collection("User").addSnapshotListener { snapshot, error in
//            if error != nil{
//                print(error!.localizedDescription)
//            }
//            else
//            {
//                if snapshot?.isEmpty != true && snapshot != nil{
//
//                    for document in snapshot!.documents{
//
//                        let documentID = document.documentID
//
//                        if let eventCategory = document.get("eventCategory") as? String{
//                            oneEvent.eventCategory = eventCategory
//                        }
//                    }
//                }
//            }
//        }
        
    }
}
