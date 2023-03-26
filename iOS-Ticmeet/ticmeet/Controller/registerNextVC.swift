//
//  registerNextVC.swift
//  ticmeet
//
//  Created by Yakup on 22.03.2023.
//

import UIKit
import Firebase
import FirebaseAuth
import FirebaseFirestore
import FirebaseStorage
import FirebaseDatabase

class registerNextVC: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet weak var popUpButtonAge: UIButton!
    @IBOutlet weak var popUpButtonGender: UIButton!
    @IBOutlet weak var nextButton: UIButton!
    
    var user = User()
    
    @IBOutlet weak var usernameTextfield: UITextField!
    @IBOutlet weak var userImageView: UIImageView!
    @IBOutlet weak var nameTextfield: UITextField!
    @IBOutlet weak var bioTextfield: UITextField!
    @IBOutlet weak var cityTextfield: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setAppearance()
        
        popUpButtonAgeFunc()
        popUpButtonGenderFunc()
        
        userImageView.isUserInteractionEnabled = true
        let imageGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(chooseImage))
        userImageView.addGestureRecognizer(imageGestureRecognizer)
        
        print("Gelen user bilgileri:")
        print("Name: \(String(describing: user.userName))")
        print("Email: \(String(describing: user.userEmail))")
        print("Password: \(String(describing: user.userPassword))")
        
        userImageView.layer.cornerRadius = 60
    }
    
    @IBAction func nextFunc(_ sender: Any) {
        if cityTextfield.text == "" || nameTextfield.text == "" || user.userAge == nil || user.userGender == nil || usernameTextfield.text == ""{
            print("Eksik yerler var")
            return
        }
        
        let storage = Storage.storage()
        let storageReference = storage.reference()
        
        let mediaFolder = storageReference.child("userImages")
        
        if let data = userImageView.image?.jpegData(compressionQuality: 0.5){
            let uuid = UUID().uuidString
            
            let imageReference = mediaFolder.child("\(uuid).jpg")
            
            imageReference.putData(data, metadata: nil) { metadata, error in
                if error != nil{
                    print(error!.localizedDescription)
                }
                else{
                    imageReference.downloadURL { url, error in
                        if error != nil{
                            print(error!.localizedDescription)
                        }
                        else{
                            let imageUrl = url?.absoluteString
                            
                            let firestoreDatabase = Firestore.firestore()
                            
                            var firestoreReference : DocumentReference? = nil
                            
                            let firestorePost = ["userID" : UUID().uuidString,
                                                 "userEmail" : self.user.userEmail!,
                                                 "userName" : self.nameTextfield.text!,
                                                 "userImage" : imageUrl!,
                                                 "userAge" : self.user.userAge!,
                                                 "userUsername" : self.usernameTextfield.text!,
                                                 "userGender" : self.user.userGender!,
                                                 "userBio" : self.bioTextfield.text ?? "",
                                                 "userLocation": self.cityTextfield.text!,
                                                 "userFollowers" : [],
                                                 "userFollowing" : [],
                                                 "userEventsID" : [],
                                                 "userRegisterDate": self.user.userRegisterDate!]
                            
                            firestoreReference = firestoreDatabase.collection("User").addDocument(data: firestorePost, completion: { error in
                                if error != nil{
                                    print(error!.localizedDescription)
                                }
                                else{
                                    print("Veri başarıyla kaydedildi")
                                    
                                    self.performSegue(withIdentifier: "toHomepage", sender: nil)
                                }
                            })
                        }
                    }
                }
            }
        }
    }
    
    @objc func chooseImage(){
        let pickerController = UIImagePickerController()
        pickerController.delegate = self
        pickerController.sourceType = .photoLibrary
        present(pickerController, animated: true)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        userImageView.image = info[.originalImage] as? UIImage
        self.dismiss(animated: true)
    }
    
    func popUpButtonGenderFunc(){
        let optionClosuree = {(action: UIAction) in
            self.user.userGender = action.title
        }
        
        popUpButtonGender.menu = UIMenu(children: [
            UIAction(title: "Belirtmek istemiyorum", handler: optionClosuree),
            UIAction(title: "Erkek", handler: optionClosuree),
            UIAction(title: "Kadın", state: .on, handler: optionClosuree)])
    }
    
    func popUpButtonAgeFunc(){
        let optionClosure = {(action: UIAction) in
            self.user.userAge = Int(action.title)!
        }
        
        popUpButtonAge.menu = UIMenu(children: [
            UIAction(title: "18", state: .on, handler: optionClosure),
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
    
    func setAppearance()
    {
        textFieldLeftImage(imageName: "usernameIcon", textField: usernameTextfield)
        textFieldLeftImage(imageName: "usernameIcon", textField: nameTextfield)
        textFieldLeftImage(imageName: "usernameIcon", textField: bioTextfield)
        textFieldLeftImage(imageName: "usernameIcon", textField: cityTextfield)
    }
    
    func textFieldLeftImage(imageName: String, textField: UITextField){
        let imageView = UIImageView(frame: CGRect(x: 8.0, y: 8.0, width: 24.0, height: 24.0))
        let image = UIImage(named: imageName);
        imageView.contentMode = .scaleAspectFit
        imageView.image = image;
        let view = UIView(frame: CGRect(x: 0, y: 0, width: 40, height: 40))
        view.addSubview(imageView)
        
        textField.leftView = view;
        textField.leftViewMode = .always
        textField.layer.cornerRadius = 20.0
        textField.layer.borderWidth = 1.5
        textField.layer.borderColor = UIColor(named: NSDataAssetName(stringLiteral: "gray"))!.cgColor
    }
}
