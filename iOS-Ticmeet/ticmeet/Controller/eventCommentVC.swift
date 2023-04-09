//
//  commentVC.swift
//  ticmeet
//
//  Created by Yakup on 29.03.2023.
//

import UIKit
import Firebase

class eventCommentVC: UIViewController {

    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var sendCommentButton: UIButton!
    @IBOutlet weak var alertLabel: UILabel!
    @IBOutlet weak var commentTextView: UITextView!
    
    var user = User()
    var selectedEvent = Event()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        getUser()
        commentTextView.layer.cornerRadius = 5
        commentTextView.layer.borderColor = UIColor.lightGray.cgColor
        commentTextView.layer.borderWidth = 1
        
        view.isUserInteractionEnabled = true
        let gestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(keyboardDismiss))
        view.addGestureRecognizer(gestureRecognizer)
    }
    
    @objc func keyboardDismiss(){
        view.endEditing(true)
    }
    
    @IBAction func backFunc(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    @IBAction func sendFunc(_ sender: Any) {
        alertLabel.isHidden = true
        
        if commentTextView.text == ""{
            alertLabel.text = "Lütfen yorum giriniz."
            alertLabel.isHidden = false
        }
        if commentTextView.text!.count >= 30{
            sendComment()
            self.dismiss(animated: true)
        }
        else{
            alertLabel.text = "Lütfen yorum için 30 karakter girin."
            alertLabel.isHidden = false
        }
    }
    
    func sendComment(){
        let firestore = Firestore.firestore()
        
        firestore.collection("Comment").addDocument(data: [
            "comment" : commentTextView.text!,
            "commentEmail" : user.userEmail!,
            "commentEventID" : selectedEvent.eventID,
            "commentUserName" : user.userName!,
            "commentUserUsername" : user.userUsername!
        ]) { error in
            if let error = error{
                print("Hata: \(error.localizedDescription)")
            }
            else{
                print("Başarılı!")
            }
        }
    }
    
    func getUser(){
        let firestore = Firestore.firestore()
        
        firestore.collection("User").whereField("userEmail", isEqualTo: (Auth.auth().currentUser?.email)!).getDocuments { querySnapshot, error in
            if let error = error{
                print("Hata: \(error.localizedDescription)")
            }
            else{
                guard let querySnapshot = querySnapshot else { return }
                
                for document in querySnapshot.documents{
                    
                    let userName = document.get("userName") as? String
                    let userUsername = document.get("userUsername") as? String
                    let userEmail = document.get("userEmail") as? String
                    
                    self.user.userEmail = userEmail
                    self.user.userName = userName
                    self.user.userUsername = userUsername
                }
            }
        }
    }
}
