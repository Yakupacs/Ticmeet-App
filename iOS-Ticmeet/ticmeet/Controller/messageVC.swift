//
//  messageVC.swift
//  ticmeet
//
//  Created by Yakup on 19.03.2023.
//

import UIKit

class messageVC: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var messageSearchBar: UISearchBar!
    @IBOutlet weak var tableView: UITableView!
    
    var messages = [Message]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.delegate = self
        tableView.dataSource = self
        
        messages.append(Message(message: "a", messageUserEmail: "ykpacs@gmail.com", messageUserName: "yakupacs", messageIsSeen: true, messageUserImage: UIImage(named: "yakup")!, messageTime: Date()))
        messages.append(Message(message: "a", messageUserEmail: "ykpacs@gmail.com", messageUserName: "yakupacs", messageIsSeen: true, messageUserImage: UIImage(named: "yakup")!, messageTime: Date()))
        messages.append(Message(message: "a", messageUserEmail: "ykpacs@gmail.com", messageUserName: "yakupacs", messageIsSeen: true, messageUserImage: UIImage(named: "yakup")!, messageTime: Date()))
        messages.append(Message(message: "a", messageUserEmail: "ykpacs@gmail.com", messageUserName: "yakupacs", messageIsSeen: true, messageUserImage: UIImage(named: "yakup")!, messageTime: Date()))
        messages.append(Message(message: "a", messageUserEmail: "ykpacs@gmail.com", messageUserName: "yakupacs", messageIsSeen: true, messageUserImage: UIImage(named: "yakup")!, messageTime: Date()))
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as? messageCell{
            cell.profileImage.image = messages[indexPath.row].messageUserImage
            cell.profileNameLabel.text = messages[indexPath.row].messageUserName
//            cell.profileSeenDateLabel.text = messages[indexPath.row].messageTime
//            cell.profileSeenTimeLabel.text = messages[indexPath.row].messageTime
            cell.profileImage.layer.cornerRadius = 30

            if messages[indexPath.row].messageIsSeen == false{
                cell.profileImage.layer.borderColor = UIColor.blue.cgColor
                cell.profileNameLabel.textColor = .blue
                cell.profileSeenTimeLabel.textColor = .blue
                cell.profileSeenDateLabel.textColor = .blue
                cell.profileSeenTimeLabel.text = "1 yeni mesaj"
                cell.profileSeenDateLabel.text = "3 dakika önce"
                cell.profileImage.layer.borderWidth = 2
            }
            else{
                cell.profileImage.layer.borderColor = UIColor(named: NSDataAssetName(stringLiteral: "borderColor"))?.cgColor
                cell.profileImage.layer.borderWidth = 2
            }

            return cell
        }
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messages.count
    }
}
