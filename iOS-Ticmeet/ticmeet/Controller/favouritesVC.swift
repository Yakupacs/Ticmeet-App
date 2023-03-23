//
//  favouritesVC.swift
//  ticmeet
//
//  Created by Yakup on 19.03.2023.
//

import UIKit
import Firebase
import FirebaseFirestore

class favouritesVC: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        if let user = Auth.auth().currentUser {
            let uid = user.uid
            let db = Firestore.firestore()
            db.collection("users").document(uid).getDocument { (document, error) in
                if let document = document, document.exists {
                    let data = document.data()
                    let userAge = data?["age"] as? Int ?? 0
                    let userDisplayName = data?["displayName"] as? String ?? "No Name"
                    print("User Age: \(userAge), User Display Name: \(userDisplayName)")
                } else {
                    print("Document does not exist")
                }
            }
        }
    }

}
