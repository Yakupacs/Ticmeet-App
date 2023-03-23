//
//  ViewController.swift
//  ticmeet
//
//  Created by Yakup on 5.03.2023.
//

import UIKit
import Firebase
import FirebaseAuth

class loginVC: UIViewController {

    @IBOutlet weak var facebookLoginButton: UIButton!
    @IBOutlet weak var loginButton: UIButton!
    @IBOutlet weak var passwordTextfield: UITextField!
    @IBOutlet weak var emailTextfield: UITextField!
    @IBOutlet weak var passwordForgetButton: UIButton!
    @IBOutlet weak var alertLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setAppearance()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        alertLabel.isHidden = true
        alertLabel.text = ""
        emailTextfield.text = ""
        passwordTextfield.text = ""
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleTap))
        view.addGestureRecognizer(tapGesture)
    }
    
    @objc func handleTap(){
        view.endEditing(true)
    }


    @IBAction func toRegisterFunc(_ sender: Any) {
        performSegue(withIdentifier: "toRegister", sender: nil)
    }
    
    @IBAction func loginFunc(_ sender: Any) {
        alertLabel.isHidden = true
        
        Auth.auth().signIn(withEmail: emailTextfield.text!, password: passwordTextfield.text!) { authData, error in
            if error != nil
            {
                UIView.animate(withDuration: 0.01, animations:{
                    self.alertLabel.isHidden = false
                    self.alertLabel.text = error!.localizedDescription
                    self.alertLabel.bounds.size.width -= 10
                }, completion: { _ in
                    UIView.animate(withDuration: 0.01) {
                        self.alertLabel.bounds.size.width += 10
                    }
                })
            }
            else{
                self.performSegue(withIdentifier: "toHomepage", sender: nil)
            }
        }
    }
    
    @IBAction func passwordResetButton(_ sender: Any) {
        performSegue(withIdentifier: "toReset", sender: nil)
    }
    
    func setAppearance()
    {
        textFieldLeftImage(imageName: "usernameIcon", textField: emailTextfield)
        textFieldLeftImage(imageName: "passwordIcon", textField: passwordTextfield)
        facebookLoginButton.layer.borderWidth = 1
        facebookLoginButton.layer.borderColor = UIColor.blue.cgColor
        facebookLoginButton.layer.cornerRadius = 21
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

