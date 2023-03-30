//
//  profileEditVC.swift
//  ticmeet
//
//  Created by Yakup on 24.03.2023.
//

import UIKit
import Firebase
import FirebaseStorage

class profileEditVC: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate  {
    
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var topImageView: UIImageView!
    @IBOutlet weak var profileImageView: UIImageView!
    @IBOutlet weak var topImageButton: UIButton!
    @IBOutlet weak var profileImageButton: UIButton!
    @IBOutlet weak var nameTextfield: UITextField!
    @IBOutlet weak var genderPopupButton: UIButton!
    @IBOutlet weak var agePopupButton: UIButton!
    @IBOutlet weak var bioTextfield: UITextField!
    @IBOutlet weak var cityTextfield: UITextField!
    @IBOutlet weak var usernameTextfield: UITextField!
    
    var gettingUser = User()
    var saveDataUser = User()
    
    var sign = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setAppearance()
        popUpButtonAgeFunc()
        popUpButtonGenderFunc()
        setUserData()
        
        view.isUserInteractionEnabled = true
        let gestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(keyboardDismiss))
        view.addGestureRecognizer(gestureRecognizer)
        
    }
    
    @IBAction func profileImageChangeFunc(_ sender: Any) {
        sign = true
        chooseImage()
    }
    
    @IBAction func topImageChangeFunc(_ sender: Any) {
        sign = false
        chooseImage()
    }
    
    @objc func chooseImage(){
        let pickerController = UIImagePickerController()
        pickerController.delegate = self
        pickerController.sourceType = .photoLibrary
        present(pickerController, animated: true)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if sign == true{
            profileImageView.image = info[.originalImage] as? UIImage
        }
        else{
            topImageView.image = info[.originalImage] as? UIImage
        }
        self.dismiss(animated: true)
    }
    
    @IBAction func saveFunc(_ sender: Any) {
        if nameTextfield.text == "" ||
            bioTextfield.text == "" ||
            cityTextfield.text == "" ||
            usernameTextfield.text == ""{
            print("Hata")
        }
        else{
            let firestore = Firestore.firestore()
            
            firestore.collection("User").whereField("userEmail", isEqualTo: (Auth.auth().currentUser?.email)!).getDocuments { querySnapshot, error in
                if let error = error{
                    print("Hata: \(error)")
                }
                else{
                    guard let querySnapshot = querySnapshot else { return }
                    
                    for document in querySnapshot.documents{
                        
                        let documentRef = document.reference
                        
                        let storage = Storage.storage()
                        let storageReference = storage.reference()
                        
                        let mediaFolder = storageReference.child("userImages")
                        
                        if let data = self.topImageView.image?.jpegData(compressionQuality: 0.5), let data2 = self.profileImageView.image?.jpegData(compressionQuality: 0.5){
                            let uuid = UUID().uuidString
                            
                            let imageReference = mediaFolder.child("\(uuid).jpg")
                            let imageReference2 = mediaFolder.child("\(uuid).jpg")
                            
                            imageReference.putData(data, metadata: nil) { metadata, error in
                                if error != nil{
                                    print(error!.localizedDescription)
                                }
                                else{
                                    imageReference2.putData(data2, metadata: nil) { metadata2, error2 in
                                        if error2 != nil{
                                            print(error2!.localizedDescription)
                                        }
                                        else{
                                            imageReference.downloadURL { url, error3 in
                                                if error3 != nil{
                                                    print(error3!.localizedDescription)
                                                }
                                                else{
                                                    
                                                    imageReference2.downloadURL { url2, error4 in
                                                        if error4 != nil{
                                                            print(error4!.localizedDescription)
                                                        }
                                                        else{
                                                            let imageUrl = url?.absoluteString
                                                            let imageUrl2 = url2?.absoluteString
                                                            
                                                            documentRef.updateData(["userAge": self.saveDataUser.userAge!,
                                                                                    "userBio": self.bioTextfield.text!,
                                                                                    "userGender": self.saveDataUser.userGender!,
                                                                                    "userLocation": self.cityTextfield.text!,
                                                                                    "userUsername": self.usernameTextfield.text!,
                                                                                    "userName": self.nameTextfield.text!,
                                                                                    "userImage": imageUrl!,
                                                                                    "userTopImage": imageUrl2!]) { error in
                                                                if let error = error {
                                                                    print("Hata: \(error)")
                                                                } else {
                                                                    print("Veriler başarıyla güncellendi")
                                                                    self.dismiss(animated: true)
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
                            
                        }
                    }
                }
            }
        }
    }
    
    
    @IBAction func backFunc(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    @objc func keyboardDismiss(){
        view.endEditing(true)
    }
    
    func setAppearance(){
        profileImageView.layer.cornerRadius = 45
        profileImageView.layer.borderColor = UIColor.white.cgColor
        profileImageView.layer.borderWidth = 2
    }
    
    func setUserData(){
        saveDataUser.userBio = gettingUser.userBio
        saveDataUser.userName = gettingUser.userName
        saveDataUser.userGender = gettingUser.userGender
        saveDataUser.userLocation = gettingUser.userLocation
        saveDataUser.userUsername = gettingUser.userUsername
        saveDataUser.userAge = gettingUser.userAge
        
        bioTextfield.text = gettingUser.userBio
        nameTextfield.text = gettingUser.userName
        cityTextfield.text = gettingUser.userLocation
        usernameTextfield.text = gettingUser.userUsername
        profileImageView.sd_setImage(with: URL(string: gettingUser.userImage!))
        topImageView.sd_setImage(with: URL(string: gettingUser.userTopImage!))
    }
    
    func popUpButtonGenderFunc(){
        let optionClosuree = {(action: UIAction) in
            if action.title != "Cinsiyet"{
                self.saveDataUser.userGender = action.title
            }
            else{
                self.saveDataUser.userGender = self.gettingUser.userGender
            }
        }
        
        genderPopupButton.menu = UIMenu(children: [
            UIAction(title: "Belirtmek istemiyorum", handler: optionClosuree),
            UIAction(title: "Erkek", handler: optionClosuree),
            UIAction(title: "Kadın", handler: optionClosuree),
            UIAction(title: "Cinsiyet", state: .on, handler: optionClosuree)])
    }
    
    func popUpButtonAgeFunc(){
        let optionClosure = {(action: UIAction) in
            if action.title != "Yaş"{
                self.saveDataUser.userAge = Int(action.title)!
            }
            else{
                self.saveDataUser.userAge = self.gettingUser.userAge
            }
        }
        
        agePopupButton.menu = UIMenu(children: [
            UIAction(title: "Yaş", state: .on, handler: optionClosure),
            UIAction(title: "18", handler: optionClosure),
            UIAction(title: "19", handler: optionClosure),
            UIAction(title: "20", handler: optionClosure),
            UIAction(title: "21", handler: optionClosure),
            UIAction(title: "22", handler: optionClosure),
            UIAction(title: "23", handler: optionClosure),
            UIAction(title: "24", handler: optionClosure),
            UIAction(title: "25", handler: optionClosure),
            UIAction(title: "26", handler: optionClosure),
            UIAction(title: "27", handler: optionClosure),
            UIAction(title: "28", handler: optionClosure),
            UIAction(title: "29", handler: optionClosure),
            UIAction(title: "30", handler: optionClosure),
            UIAction(title: "31", handler: optionClosure),
            UIAction(title: "32", handler: optionClosure),
            UIAction(title: "33", handler: optionClosure),
            UIAction(title: "34", handler: optionClosure),
            UIAction(title: "35", handler: optionClosure),
            UIAction(title: "36", handler: optionClosure),
            UIAction(title: "37", handler: optionClosure),
            UIAction(title: "38", handler: optionClosure),
            UIAction(title: "39", handler: optionClosure),
            UIAction(title: "40", handler: optionClosure),
            UIAction(title: "41", handler: optionClosure),
            UIAction(title: "42", handler: optionClosure),
            UIAction(title: "43", handler: optionClosure),
            UIAction(title: "44", handler: optionClosure),
            UIAction(title: "45", handler: optionClosure),
            UIAction(title: "46", handler: optionClosure),
            UIAction(title: "47", handler: optionClosure),
            UIAction(title: "48", handler: optionClosure),
            UIAction(title: "49", handler: optionClosure),
            UIAction(title: "50", handler: optionClosure),
            UIAction(title: "51", handler: optionClosure),
            UIAction(title: "52", handler: optionClosure),
            UIAction(title: "53", handler: optionClosure),
            UIAction(title: "54", handler: optionClosure),
            UIAction(title: "55", handler: optionClosure),
            UIAction(title: "56", handler: optionClosure),
            UIAction(title: "57", handler: optionClosure),
            UIAction(title: "58", handler: optionClosure),
            UIAction(title: "59", handler: optionClosure),
            UIAction(title: "60", handler: optionClosure),])
    }
}
