//
//  DataService.swift
//  ticmeet
//
//  Created by Yakup on 16.03.2023.
//

import Foundation
import Firebase
import UIKit
import FirebaseAuth

class DataService
{
    static let instance = DataService()
    var ref : DatabaseReference? = Database.database().reference()
    
    //    func registerNewUser(user: User)
    //    {
    //        let imageData = user.userImage!.jpegData(compressionQuality: 0.5)!
    //        let base64String = imageData.base64EncodedString(options: .lineLength64Characters)
    //
    //        let dict: [String:Any] = ["userName": user.userName!, "userEmail": user.userEmail!, "userUsername": user.userUsername!, "userPassword": user.userPassword!, "userImage": base64String]
    //
    //        let newRef = ref?.child("user").childByAutoId()
    //
    //        newRef?.setValue(dict)
    //
    //        print("Veri başarıyla kaydedildi:\nName: \(user.userName!)\nEmail: \(user.userEmail!)\nUsername: \(user.userUsername!)\nPassword: \(user.userPassword!)")
    //    }
    //
    //    // Tüm verileri okuma
    //    func readAllUsers()
    //    {
    //
    //        ref?.child("user").observe(.value, with: { snapshot in
    //            print("------USERS------")
    //            if let allData = snapshot.value as? [String:AnyObject]
    //            {
    //                for cv in allData
    //                {
    //                    if let dict = cv.value as? NSDictionary
    //                    {
    //                        let key = cv.key
    //                        let userName = dict["userName"] as? String ?? ""
    //                        let userEmail = dict["userEmail"] as? String ?? ""
    //                        let userUsername = dict["userUsername"] as? String ?? ""
    //                        let userPassword = dict["userPassword"] as? String ?? ""
    //                        let base64String = dict["userImage"] as? String ?? ""
    //
    //                        if let imageData = Data(base64Encoded: base64String),
    //                           let _ = UIImage(data: imageData) {
    //                            print("\(userUsername) Resim verisi başarıyla çözümlendi")
    //                        } else {
    //                            print("\(userUsername) Geçersiz base64 verisi")
    //                        }
    //
    //                        print("\(key), \(userUsername), \(userName), \(userEmail), \(userPassword)")
    //                        print("")
    //                    }
    //                }
    //            }
    //            print("-----------------")
    //        })
    //
    //    }
    //
    //    // Belirli bir kişinin verilerini getirme
    //    func isHaveUser(user: User)
    //    {
    //        let query = ref?.child("user").queryOrdered(byChild: "userUsername").queryEqual(toValue: user.userUsername)
    //
    //        query!.observe(.value, with: { snapshot in
    //            if let allData = snapshot.value as? [String:AnyObject]
    //            {
    //                for cv in allData
    //                {
    //                    if let dict = cv.value as? NSDictionary
    //                    {
    //                        let key = cv.key
    //                        let userName = dict["userName"] as? String ?? ""
    //                        let userEmail = dict["userEmail"] as? String ?? ""
    //                        let userUsername = dict["userUsername"] as? String ?? ""
    //                        let userPassword = dict["userPassword"] as? String ?? ""
    //
    //                        print("\(key), \(userUsername), \(userName), \(userEmail), \(userPassword)")
    //                    }
    //                }
    //            }
    //        })
    //
    //    }
    
    
}
