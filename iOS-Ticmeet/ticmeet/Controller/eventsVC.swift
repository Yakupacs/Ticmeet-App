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
import FirebaseStorage

class eventsVC: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var konserCollectionView: UICollectionView!
    @IBOutlet weak var tiyatroCollectionView: UICollectionView!
    @IBOutlet weak var sinemaCollectionView: UICollectionView!
    @IBOutlet weak var soylesiCollectionView: UICollectionView!
    @IBOutlet weak var sergiCollectionView: UICollectionView!
    @IBOutlet weak var operaCollectionView: UICollectionView!
    @IBOutlet weak var performansCollectionView: UICollectionView!
    @IBOutlet weak var baleCollectionView: UICollectionView!
    
    var user = User()
    
    var allEvents = [Event]()
    
    var konserEvents = [Event]()
    var tiyatroEvents = [Event]()
    var sinemaEvents = [Event]()
    var soylesiEvents = [Event]()
    var sergiEvents = [Event]()
    var operaEvents = [Event]()
    var performansEvents = [Event]()
    var baleEvents = [Event]()
    
    var selectedEvent = Event()
    var selectedCategory = Event()
    
    var myUserEventsID = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        getFirestoreDataFirebase()
        
        konserCollectionView.delegate = self
        konserCollectionView.dataSource = self
        tiyatroCollectionView.delegate = self
        tiyatroCollectionView.dataSource = self
        sinemaCollectionView.delegate = self
        sinemaCollectionView.dataSource = self
        soylesiCollectionView.delegate = self
        soylesiCollectionView.dataSource = self
        sergiCollectionView.delegate = self
        sergiCollectionView.dataSource = self
        operaCollectionView.delegate = self
        operaCollectionView.dataSource = self
        performansCollectionView.delegate = self
        performansCollectionView.dataSource = self
        baleCollectionView.delegate = self
        baleCollectionView.dataSource = self
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == self.konserCollectionView{
            return konserEvents.count
        }
        else if collectionView == self.tiyatroCollectionView{
            return tiyatroEvents.count
        }
        else if collectionView == self.sinemaCollectionView{
            return sinemaEvents.count
        }
        else if collectionView == self.soylesiCollectionView{
            return soylesiEvents.count
        }
        else if collectionView == self.sergiCollectionView{
            return sergiEvents.count
        }
        else if collectionView == self.operaCollectionView{
            return operaEvents.count
        }
        else if collectionView == self.performansCollectionView{
            return performansEvents.count
        }
        else{
            return baleEvents.count
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == self.konserCollectionView{
            if let cell = konserCollectionView.dequeueReusableCell(withReuseIdentifier: "cellKonser", for: indexPath) as? konserCell{
                
                if konserEvents[indexPath.row].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
                    cell.addButton.setImage(UIImage(named: "checked50x50"), for: .normal)
                }
                else{
                    cell.addButton.setImage(UIImage(named: "plus50x50"), for: .normal)
                }
                
                cell.eventUserCountLabel.text = String(konserEvents[indexPath.row].eventAttented)
                cell.eventImageView.sd_setImage(with: URL(string: konserEvents[indexPath.row].eventImage))
                cell.eventImageView.layer.cornerRadius = 20
                cell.userCountView.layer.cornerRadius = 12
                cell.addButton.tag = indexPath.row
                cell.addButton.addTarget(self, action: #selector(addEventKonser), for: .touchUpInside)
                return cell
            }
        }
        else if collectionView == self.tiyatroCollectionView{
            if let cell = tiyatroCollectionView.dequeueReusableCell(withReuseIdentifier: "cellTiyatro", for: indexPath) as? tiyatroCell{
                
                if tiyatroEvents[indexPath.row].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
                    cell.addButton.setImage(UIImage(named: "checked50x50"), for: .normal)
                }
                else{
                    cell.addButton.setImage(UIImage(named: "plus50x50"), for: .normal)
                }
                
                cell.eventUserCountLabel.text = String(tiyatroEvents[indexPath.row].eventAttented)
                cell.eventImageView.sd_setImage(with: URL(string: tiyatroEvents[indexPath.row].eventImage))
                cell.eventImageView.layer.cornerRadius = 20
                cell.userCountView.layer.cornerRadius = 12
                cell.addButton.tag = indexPath.row
                cell.addButton.addTarget(self, action: #selector(addEventTiyatro), for: .touchUpInside)
                return cell
            }
        }
        else if collectionView == self.sinemaCollectionView{
            if let cell = sinemaCollectionView.dequeueReusableCell(withReuseIdentifier: "cellSinema", for: indexPath) as? sinemaCell{
                
                if sinemaEvents[indexPath.row].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
                    cell.addButton.setImage(UIImage(named: "checked50x50"), for: .normal)
                }
                else{
                    cell.addButton.setImage(UIImage(named: "plus50x50"), for: .normal)
                }
                
                cell.eventUserCountLabel.text = String(sinemaEvents[indexPath.row].eventAttented)
                cell.eventImageView.sd_setImage(with: URL(string: sinemaEvents[indexPath.row].eventImage))
                cell.eventImageView.layer.cornerRadius = 20
                cell.userCountView.layer.cornerRadius = 12
                cell.addButton.tag = indexPath.row
                cell.addButton.addTarget(self, action: #selector(addEventSinema), for: .touchUpInside)
                return cell
            }
        }
        else if collectionView == self.soylesiCollectionView{
            if let cell = soylesiCollectionView.dequeueReusableCell(withReuseIdentifier: "cellSoylesi", for: indexPath) as? soylesiCell{
                
                if soylesiEvents[indexPath.row].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
                    cell.addButton.setImage(UIImage(named: "checked50x50"), for: .normal)
                }
                else{
                    cell.addButton.setImage(UIImage(named: "plus50x50"), for: .normal)
                }
                
                cell.eventUserCountLabel.text = String(soylesiEvents[indexPath.row].eventAttented)
                cell.eventImageView.sd_setImage(with: URL(string: soylesiEvents[indexPath.row].eventImage))
                cell.eventImageView.layer.cornerRadius = 20
                cell.userCountView.layer.cornerRadius = 12
                cell.addButton.tag = indexPath.row
                cell.addButton.addTarget(self, action: #selector(addEventSoylesi), for: .touchUpInside)
                return cell
            }
        }
        else if collectionView == self.sergiCollectionView{
            if let cell = sergiCollectionView.dequeueReusableCell(withReuseIdentifier: "cellSergi", for: indexPath) as? sergiCell{
                
                if sergiEvents[indexPath.row].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
                    cell.addButton.setImage(UIImage(named: "checked50x50"), for: .normal)
                }
                else{
                    cell.addButton.setImage(UIImage(named: "plus50x50"), for: .normal)
                }
                
                cell.eventUserCountLabel.text = String(sergiEvents[indexPath.row].eventAttented)
                cell.eventImageView.sd_setImage(with: URL(string: sergiEvents[indexPath.row].eventImage))
                cell.eventImageView.layer.cornerRadius = 20
                cell.userCountView.layer.cornerRadius = 12
                cell.addButton.tag = indexPath.row
                cell.addButton.addTarget(self, action: #selector(addEventSergi), for: .touchUpInside)
                return cell
            }
        }
        else if collectionView == self.operaCollectionView{
            if let cell = operaCollectionView.dequeueReusableCell(withReuseIdentifier: "cellOpera", for: indexPath) as? operaCell{
                
                if operaEvents[indexPath.row].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
                    cell.addButton.setImage(UIImage(named: "checked50x50"), for: .normal)
                }
                else{
                    cell.addButton.setImage(UIImage(named: "plus50x50"), for: .normal)
                }
                
                cell.eventUserCountLabel.text = String(operaEvents[indexPath.row].eventAttented)
                cell.eventImageView.sd_setImage(with: URL(string: operaEvents[indexPath.row].eventImage))
                cell.eventImageView.layer.cornerRadius = 20
                cell.userCountView.layer.cornerRadius = 12
                cell.addButton.tag = indexPath.row
                cell.addButton.addTarget(self, action: #selector(addEventOpera), for: .touchUpInside)
                return cell
            }
        }
        else if collectionView == self.performansCollectionView{
            if let cell = performansCollectionView.dequeueReusableCell(withReuseIdentifier: "cellPerformans", for: indexPath) as? peformansCell{
                
                if performansEvents[indexPath.row].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
                    cell.addButton.setImage(UIImage(named: "checked50x50"), for: .normal)
                }
                else{
                    cell.addButton.setImage(UIImage(named: "plus50x50"), for: .normal)
                }
                
                cell.eventUserCountLabel.text = String(performansEvents[indexPath.row].eventAttented)
                cell.eventImageView.sd_setImage(with: URL(string: performansEvents[indexPath.row].eventImage))
                cell.eventImageView.layer.cornerRadius = 20
                cell.userCountView.layer.cornerRadius = 12
                cell.addButton.tag = indexPath.row
                cell.addButton.addTarget(self, action: #selector(addEventPerformans), for: .touchUpInside)
                return cell
            }
        }
        else if collectionView == self.baleCollectionView{
            if let cell = baleCollectionView.dequeueReusableCell(withReuseIdentifier: "cellBale", for: indexPath) as? baleCell{
                
                if baleEvents[indexPath.row].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
                    cell.addButton.setImage(UIImage(named: "checked50x50"), for: .normal)
                }
                else{
                    cell.addButton.setImage(UIImage(named: "plus50x50"), for: .normal)
                }
                
                cell.eventUserCountLabel.text = String(baleEvents[indexPath.row].eventAttented)
                cell.eventImageView.sd_setImage(with: URL(string: baleEvents[indexPath.row].eventImage))
                cell.eventImageView.layer.cornerRadius = 20
                cell.userCountView.layer.cornerRadius = 12
                cell.addButton.tag = indexPath.row
                cell.addButton.addTarget(self, action: #selector(addEventBale), for: .touchUpInside)
                return cell
            }
        }
        return UICollectionViewCell()
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == self.baleCollectionView{
            selectedEvent = baleEvents[indexPath.row]
        }
        else if collectionView == self.performansCollectionView{
            selectedEvent = performansEvents[indexPath.row]
        }
        else if collectionView == self.operaCollectionView{
            selectedEvent = operaEvents[indexPath.row]
        }
        else if collectionView == self.sergiCollectionView{
            selectedEvent = sergiEvents[indexPath.row]
        }
        else if collectionView == self.soylesiCollectionView{
            selectedEvent = soylesiEvents[indexPath.row]
        }
        else if collectionView == self.sinemaCollectionView{
            selectedEvent = sinemaEvents[indexPath.row]
        }
        else if collectionView == self.tiyatroCollectionView{
            selectedEvent = tiyatroEvents[indexPath.row]
        }
        else if collectionView == self.konserCollectionView{
            selectedEvent = konserEvents[indexPath.row]
        }
        performSegue(withIdentifier: "toDetail", sender: nil)
    }
    
    @objc func addEventTiyatro(sender: UIButton){
        if tiyatroEvents[sender.tag].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "plus50x50"), for: .normal)
                sender.alpha = 1
            }
            addEventUser(sender: sender, selectedEvent: tiyatroEvents)
        }
        else{
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "checked50x50"), for: .normal)
                sender.alpha = 1
            }
            removeEventUser(sender: sender, selectedEvent: tiyatroEvents)
        }
    }
    
    @objc func addEventKonser(sender: UIButton){
        if konserEvents[sender.tag].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "plus50x50"), for: .normal)
                sender.alpha = 1
            }
            addEventUser(sender: sender, selectedEvent: konserEvents)
        }
        else{
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "checked50x50"), for: .normal)
                sender.alpha = 1
            }
            removeEventUser(sender: sender, selectedEvent: konserEvents)
        }
    }
    
    @objc func addEventOpera(sender: UIButton){
        if operaEvents[sender.tag].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "plus50x50"), for: .normal)
                sender.alpha = 1
            }
            addEventUser(sender: sender, selectedEvent: operaEvents)
        }
        else{
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "checked50x50"), for: .normal)
                sender.alpha = 1
            }
            removeEventUser(sender: sender, selectedEvent: operaEvents)
        }
    }
    
    @objc func addEventSoylesi(sender: UIButton){
        if soylesiEvents[sender.tag].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "plus50x50"), for: .normal)
                sender.alpha = 1
            }
            addEventUser(sender: sender, selectedEvent: soylesiEvents)
        }
        else{
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "checked50x50"), for: .normal)
                sender.alpha = 1
            }
            removeEventUser(sender: sender, selectedEvent: soylesiEvents)
        }
    }
    
    @objc func addEventSergi(sender: UIButton){
        if sergiEvents[sender.tag].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "plus50x50"), for: .normal)
                sender.alpha = 1
            }
            addEventUser(sender: sender, selectedEvent: sergiEvents)
        }
        else{
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "checked50x50"), for: .normal)
                sender.alpha = 1
            }
            removeEventUser(sender: sender, selectedEvent: sergiEvents)
        }
    }
    
    @objc func addEventSinema(sender: UIButton){
        if sinemaEvents[sender.tag].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "plus50x50"), for: .normal)
                sender.alpha = 1
            }
            addEventUser(sender: sender, selectedEvent: sinemaEvents)
        }
        else{
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "checked50x50"), for: .normal)
                sender.alpha = 1
            }
            removeEventUser(sender: sender, selectedEvent: sinemaEvents)
        }
    }
    
    @objc func addEventBale(sender: UIButton){
        if baleEvents[sender.tag].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "plus50x50"), for: .normal)
                sender.alpha = 1
            }
            addEventUser(sender: sender, selectedEvent: baleEvents)
        }
        else{
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "checked50x50"), for: .normal)
                sender.alpha = 1
            }
            removeEventUser(sender: sender, selectedEvent: baleEvents)
        }
    }
    
    @objc func addEventPerformans(sender: UIButton){
        if performansEvents[sender.tag].eventUsersEmail.contains(Auth.auth().currentUser?.email ?? "") {
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "plus50x50"), for: .normal)
                sender.alpha = 1
            }
            addEventUser(sender: sender, selectedEvent: performansEvents)
        }
        else{
            sender.alpha = 0
            UIView.animate(withDuration: 0.3) {
                sender.setImage(UIImage(named: "checked50x50"), for: .normal)
                sender.alpha = 1
            }
            removeEventUser(sender: sender, selectedEvent: performansEvents)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "toDetail"{
            let destVC = segue.destination as! eventDetailVC
            destVC.getEvent = selectedEvent
        }
    }
    
    func getFirestoreDataFirebase(){
        let fireStoreDatabase = Firestore.firestore()
        fireStoreDatabase.collection("Event").addSnapshotListener { snapshot, error in
            if error != nil{
                print(error!.localizedDescription)
            }
            else
            {
                if snapshot?.isEmpty != true && snapshot != nil{
                    
                    self.allEvents = []
                    self.baleEvents = []
                    self.operaEvents = []
                    self.sergiEvents = []
                    self.konserEvents = []
                    self.soylesiEvents = []
                    self.tiyatroEvents = []
                    self.sinemaEvents = []
                    self.performansEvents = []
                    
                    for document in snapshot!.documents{
                        
                        let oneEvent = Event()
                        
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
                        
                        if oneEvent.eventCategory == "Bale"{
                            self.baleEvents.append(oneEvent)
                        }
                        else if oneEvent.eventCategory == "Opera"{
                            self.operaEvents.append(oneEvent)
                        }
                        else if oneEvent.eventCategory == "Sergi"{
                            self.sergiEvents.append(oneEvent)
                        }
                        else if oneEvent.eventCategory == "Konser"{
                            self.konserEvents.append(oneEvent)
                        }
                        else if oneEvent.eventCategory == "Söyleşi"{
                            self.soylesiEvents.append(oneEvent)
                        }
                        else if oneEvent.eventCategory == "Tiyatro"{
                            self.tiyatroEvents.append(oneEvent)
                        }
                        else if oneEvent.eventCategory == "Sinema"{
                            self.sinemaEvents.append(oneEvent)
                        }
                        else if oneEvent.eventCategory == "Performans"{
                            self.performansEvents.append(oneEvent)
                        }
                        
                        self.allEvents.append(oneEvent)
                    }
                    
                    self.konserCollectionView.reloadData()
                    self.performansCollectionView.reloadData()
                    self.sinemaCollectionView.reloadData()
                    self.tiyatroCollectionView.reloadData()
                    self.soylesiCollectionView.reloadData()
                    self.sergiCollectionView.reloadData()
                    self.operaCollectionView.reloadData()
                    self.baleCollectionView.reloadData()
                }
            }
        }
        
    }
    
    func addEventUser(sender: UIButton, selectedEvent: [Event]){
        let firestoreDatabase = Firestore.firestore()
        
        firestoreDatabase.collection("Event")
            .whereField("eventName", isEqualTo: selectedEvent[sender.tag].eventName)
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
    
    func removeEventUser(sender: UIButton, selectedEvent: [Event]){
        let firestoreDatabase = Firestore.firestore()
        
        firestoreDatabase.collection("Event")
            .whereField("eventName", isEqualTo: selectedEvent[sender.tag].eventName)
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
