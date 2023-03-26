//
//  registerVC.swift
//  ticmeet
//
//  Created by Yakup on 16.03.2023.
//

import UIKit
import FirebaseAuth
import Firebase

class registerVC: UIViewController {
    
    @IBOutlet weak var emailTextfield: UITextField!
    @IBOutlet weak var passwordTextfield: UITextField!
    @IBOutlet weak var password2Textfield: UITextField!
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var facebookRegisterButton: UIButton!
    @IBOutlet weak var alertLabel: UILabel!
    
    var user = User()
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        setAppearance()
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleTap))
        view.addGestureRecognizer(tapGesture)
    }
    
    @objc func handleTap(){
        view.endEditing(true)
    }
    
    @IBAction func returnLoginFunc(_ sender: Any) {
        performSegue(withIdentifier: "toLogin", sender: nil)
    }
    
    @IBAction func registerFunc(_ sender: Any) {
        alertLabel.isHidden = true
        alertLabel.textColor = .red
        
        if emailTextfield.text != "" && passwordTextfield.text != "" && password2Textfield.text != ""{
            
            if passwordTextfield.text == password2Textfield.text{
                
                Auth.auth().createUser(withEmail: emailTextfield.text!, password: passwordTextfield.text!) { authdata, error in
                    if error != nil
                    {
                        self.alertLabel.text = error!.localizedDescription
                        self.alertLabel.isHidden = false
                    }
                    else{
                        self.user.userEmail = self.emailTextfield.text!
                        self.user.userPassword = self.passwordTextfield.text!
                        self.user.userImage = UIImage(named: "userSelectImage")
                        self.user.userFollowers = []
                        self.user.userFollowing = []
                        self.user.userName = "Unknown"
                        self.user.userRegisterDate = Date()
                        self.performSegue(withIdentifier: "toRegister2", sender: nil)
                    }
                }
                
            }
            else
            {
                self.alertLabel.text = "Girilen şifreler aynı değil"
                self.alertLabel.isHidden = false
            }
        }
        else{
            self.alertLabel.text = "Lütfen ilgili alanları doldurunuz"
            self.alertLabel.isHidden = false
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "toRegister2"{
            let destVC = segue.destination as! registerNextVC
            destVC.user = user
        }
    }
    
    func setAppearance()
    {
        textFieldLeftImage(imageName: "emailIcon", textField: emailTextfield)
        textFieldLeftImage(imageName: "passwordIcon", textField: passwordTextfield)
        textFieldLeftImage(imageName: "passwordIcon", textField: password2Textfield)
        facebookRegisterButton.layer.borderWidth = 1
        facebookRegisterButton.layer.borderColor = UIColor.blue.cgColor
        facebookRegisterButton.layer.cornerRadius = 21
    }
    
    func clearTextfields()
    {
        emailTextfield.text = ""
        passwordTextfield.text = ""
        password2Textfield.text = ""
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
