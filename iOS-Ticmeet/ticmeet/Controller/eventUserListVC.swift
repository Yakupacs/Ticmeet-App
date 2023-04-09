//
//  usersListVC.swift
//  ticmeet
//
//  Created by Yakup on 27.03.2023.
//
import Firebase
import UIKit

class eventUserListVC: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var headerLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    
    var users = [User]()
    var selectedUser = User()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    override func viewWillAppear(_ animated: Bool) {
        tableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as? userListCell{
            
            cell.userImageView.sd_setImage(with: URL(string: users[indexPath.row].userImage!))
            cell.userActionButton.isHidden = false
            cell.userNameLabel.text = users[indexPath.row].userName
            cell.userUsernameLabel.text = "@\(users[indexPath.row].userUsername!)"
            if users[indexPath.row].userEventsID != nil{
                cell.userEventCountLabel.text = "\(users[indexPath.row].userEventsID!.count) etkinliğe katıldı."
            }
            else{
                cell.userEventCountLabel.text = "Hiç etkinliğe katılmadı."
            }
            cell.userImageView.layer.cornerRadius = 35
            cell.userImageView.layer.borderWidth = 2
            cell.userImageView.layer.borderColor = UIColor.white.cgColor
            cell.userActionButton.addTarget(self, action: #selector(userAction), for: .touchUpInside)
            cell.userActionButton.tag = indexPath.row
            
            if users[indexPath.row].userEmail == Auth.auth().currentUser?.email{
                cell.userActionButton.isHidden = true
            }
            
            if users[indexPath.row].userFollowers?.contains((Auth.auth().currentUser?.email)!) == true{
                cell.userActionButton.setTitle("Takiptesin", for: .normal)
            }
            else{
                cell.userActionButton.setTitle("Takip et", for: .normal)
            }
            return cell
        }
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedUser = users[indexPath.row]
        performSegue(withIdentifier: "toUserDetail", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "toUserDetail"{
            let destVC = segue.destination as! profileVC
            destVC.user = selectedUser
        }
    }
    
    @objc func userAction(sender: UIButton){
        
        if users[sender.tag].userFollowers?.contains((Auth.auth().currentUser?.email)!) == true{
            
            removeEventUser(selectedUser: users[sender.tag])
            print("\(users[sender.tag].userName!) kişisini takipten bıraktın.")
            users[sender.tag].userFollowers = users[sender.tag].userFollowers!.filter { $0 != Auth.auth().currentUser?.email }
            
            tableView.reloadData()
        }
        else{
            
            addFollowUser(addUser: users[sender.tag])
            print("\(users[sender.tag].userName!) kişisini takip ettin.")
            users[sender.tag].userFollowers?.append((Auth.auth().currentUser?.email)!)
            
            tableView.reloadData()
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return users.count
    }
    
    @IBAction func backFunc(_ sender: Any) {
        
        self.dismiss(animated: true)
    }
    
    func addFollowUser(addUser: User){
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
    
    func removeEventUser(selectedUser: User){
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
    
}
