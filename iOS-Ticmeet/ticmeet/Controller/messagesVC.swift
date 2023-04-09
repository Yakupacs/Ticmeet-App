//
//  messageVC.swift
//  ticmeet
//
//  Created by Yakup on 19.03.2023.
//

import UIKit
import Firebase

class messagesVC: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var messageSearchBar: UISearchBar!
    @IBOutlet weak var tableView: UITableView!
    
    var messages = [Message]()
    var otherUsers = [User]()
    var selectedUserEmail = String()
    var selectedUserImage = String()
    var selectedUserName = String()
    var selectedUserUsername = String()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
        
        getMessages()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        tableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as? messageCell{
            cell.profileImage.sd_setImage(with: URL(string: messages[indexPath.row].messageUserImage))
            cell.profileNameLabel.text = messages[indexPath.row].messageUserName
            cell.profileImage.layer.cornerRadius = 30
            
            
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "dd/MM/yyyy HH:mm:ss"
            let messageTime = messages[indexPath.row].messageTime
            let dateString = dateFormatter.string(from: messageTime)
            
            // 1. Anlık zamanı alarak, mesaj zamanından ne kadar süre önce olduğunu hesaplayalım
            let currentTime = Date()
            let timeDifference = currentTime.timeIntervalSince(messageTime)
            
            // 2. Süreyi kontrol edelim ve uygun bir şekilde formatlayalım
            let secondsInAMinute: Double = 60
            let secondsInAnHour = secondsInAMinute * 60
            let secondsInADay = secondsInAnHour * 24
            let secondsInAWeek = secondsInADay * 7
            let secondsInAMonth = secondsInAWeek * 4

            if timeDifference < secondsInAMinute {
                cell.profileSeenTimeLabel.text = "\(Int(timeDifference)) saniye önce"
            } else if timeDifference < secondsInAnHour {
                let minutes = Int(timeDifference / secondsInAMinute)
                cell.profileSeenTimeLabel.text = "\(minutes) dakika önce"
            } else if timeDifference < secondsInADay {
                let hours = Int(timeDifference / secondsInAnHour)
                cell.profileSeenTimeLabel.text = "\(hours) saat önce"
            } else if timeDifference < secondsInAWeek {
                let days = Int(timeDifference / secondsInADay)
                cell.profileSeenTimeLabel.text = "\(days) gün önce"
            } else if timeDifference < secondsInAMonth {
                let months = Int(timeDifference / secondsInADay)
                cell.profileSeenTimeLabel.text = "\(months) ay önce"
            } else {
                dateFormatter.dateFormat = "dd/MM/yyyy"
                cell.profileSeenTimeLabel.text = dateFormatter.string(from: messageTime)
            }
            
            if messages[indexPath.row].messageIsSeen == true{
                cell.profileImage.layer.borderColor = UIColor.blue.cgColor
                cell.profileNameLabel.textColor = .blue
                cell.profileSeenTimeLabel.textColor = .blue
                cell.profileSeenTimeLabel.text = "1 yeni mesaj"
                cell.profileImage.layer.borderWidth = 1
            }
            else{
                cell.profileImage.layer.borderColor = UIColor(named: NSDataAssetName(stringLiteral: "borderColor"))?.cgColor
                cell.profileImage.layer.borderWidth = 1
            }
            
            return cell
        }
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedUserEmail = messages[indexPath.row].messageUserEmail
        selectedUserImage = messages[indexPath.row].messageUserImage
        selectedUserName = messages[indexPath.row].messageUserName
        selectedUserUsername = messages[indexPath.row].messageUserUsername
        performSegue(withIdentifier: "toMessage", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "toMessage"{
            let destVC = segue.destination as! messageVC
            
            let user = User()
            user.userEmail = selectedUserEmail
            user.userImage = selectedUserImage
            user.userName = selectedUserName
            user.userUsername = selectedUserUsername
            destVC.otherUser = user
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 85
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messages.count
    }
    
    func getMessages(){
        let firestore = Firestore.firestore()
        
        firestore.collection("Message").whereField("usersEmail", arrayContainsAny: [(Auth.auth().currentUser?.email)!]).addSnapshotListener { querySnapshot, error in
            self.messages = []
            if let error = error{
                print("Hata: \(error.localizedDescription)")
            }
            else{
                
                guard let querySnapshot = querySnapshot else { return }
                // Ana kullanıcının mesajlaştığı documentler
                for document in querySnapshot.documents{
                    // Her bir mesaj documenti
                    if let usersEmail = document.get("usersEmail") as? [String]{
                        // Mesaj documentindeki emailler
                        for email in usersEmail{
                            // Her bir email string
                            if (Auth.auth().currentUser?.email)! != email{
                                // Email stringi ana kullanıcıya ait değilse ikinci kullanıcınındır.
                                // Buradan ikinci kullanıcının emailini bulduk.
                                let otherUserEmail = email
                                
                                firestore.collection("User").whereField("userEmail", isEqualTo: otherUserEmail).getDocuments { querySnapshotUser, err in
                                    // Bulduğumuz emaile ait kullanıcının verilerini alıyoruz.
                                    if let err = err{
                                        print("Hata: \(err.localizedDescription)")
                                    }
                                    else{
                                        
                                        guard let querySnapshotUser = querySnapshotUser else { return }
                                        
                                        for doc in querySnapshotUser.documents{
                                            let message = Message()
                                            
                                            if let messages = document.get("messages") as? [[String: Any]]{
                                                let lastMessage = messages.last
                                                
                                                for val in lastMessage!{
                                                    
                                                    if val.key == "message"{
                                                        message.message = val.value as! String
                                                    }
                                                    else if val.key == "messageSeen"{
                                                        message.messageIsSeen = val.value as! Bool
                                                    }
                                                    else if val.key == "messageUserEmail"{
                                                        message.messageUserEmail = val.value as! String
                                                    }
                                                    else if val.key == "timestamp" {
                                                        if let timestamp = val.value as? Timestamp {
                                                            let seconds = Double(timestamp.seconds)
                                                            let nanoseconds = Double(timestamp.nanoseconds) / 1_000_000_000
                                                            message.messageTime = Date(timeIntervalSince1970: seconds + nanoseconds)
                                                        }
                                                    }
                                                    
                                                }
                                            }
                                            
                                            if let userImage = doc.get("userImage") as? String{
                                                message.messageUserImage = userImage
                                            }
                                            if let userName = doc.get("userName") as? String{
                                                message.messageUserName = userName
                                            }
                                            if let userUsername = doc.get("userUsername") as? String{
                                                message.messageUserUsername = userUsername
                                            }
                                            if let userEmail = doc.get("userEmail") as? String{
                                                message.messageUserEmail = userEmail
                                            }
                                            
                                            self.messages.append(message)
                                        }
                                        self.messages.sort{ $0.messageTime > $1.messageTime }
                                        self.tableView.reloadData()
                                    }
                                }
                                
                            }
                            
                        }
                        
                    }
                    
                }
                
            }
        }
    }
}
