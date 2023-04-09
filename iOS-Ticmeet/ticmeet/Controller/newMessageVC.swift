//
//  messageSendListVC.swift
//  ticmeet
//
//  Created by Yakup on 1.04.2023.
//

import UIKit
import Firebase

class newMessageVC: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var tableView: UITableView!
    
    var users = [User]()
    var selectedUser = User()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.delegate = self
        tableView.dataSource = self
        getData()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return users.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as? messageSendListCell{
            cell.userImageView.sd_setImage(with: URL(string: users[indexPath.row].userImage!))
            cell.userNameLabel.text = users[indexPath.row].userName
            cell.userUsernameLabel.text = users[indexPath.row].userUsername
            cell.userImageView.layer.cornerRadius = 35
            cell.userImageView.layer.borderColor = UIColor.black.cgColor
            cell.userImageView.layer.borderWidth = 1
            cell.sendMessageButton.tag = indexPath.row
            cell.sendMessageButton.addTarget(self, action: #selector(sendMessage), for: .touchUpInside)
            return cell
        }
        return UITableViewCell()
    }
    
    @objc func sendMessage(sender: UIButton){
        selectedUser = users[sender.tag]
        performSegue(withIdentifier: "toMessage", sender: nil)
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedUser = users[indexPath.row]
        performSegue(withIdentifier: "toMessage", sender: nil)
    }

    @IBAction func backFun(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "toMessage"{
            let destVC = segue.destination as! messageVC
            destVC.otherUser = selectedUser
        }
    }
    
    func getData(){
        let firestore = Firestore.firestore()
        
        firestore.collection("User").getDocuments { querySnapshot, error in
            if let error = error{
                print("Hata: \(error.localizedDescription)")
            }
            else{
                self.users = []
                
                guard let querySnapshot = querySnapshot else { return }
                
                
                for document in querySnapshot.documents{
                    
                    var user = User()
                    
                    if let userFollowers = document.get("userFollowers") as? [String]{
                        
                        for follower in userFollowers{
                            
                            if follower == Auth.auth().currentUser?.email!{
                                
                                if let userName = document.get("userName") as? String{
                                    user.userName = userName
                                }
                                
                                if let userUsername = document.get("userUsername") as? String{
                                    user.userUsername = userUsername
                                }
                                
                                if let userImage = document.get("userImage") as? String{
                                    user.userImage = userImage
                                }
                                
                                if let userEmail = document.get("userEmail") as? String{
                                    user.userEmail = userEmail
                                }
                                
                                self.users.append(user)
                                
                            }
                            
                        }
                        
                    }
                    
                }
                
                self.tableView.reloadData()
            }
        }
    }
}
