//
//  restartPasswordVC.swift
//  ticmeet
//
//  Created by Yakup on 18.03.2023.
//

import UIKit
import Firebase
import FirebaseAuth

class restartPasswordVC: UIViewController {

    @IBOutlet weak var emailTextfield: UITextField!
    @IBOutlet weak var restartPasswordButton: UIButton!
    @IBOutlet weak var alertLabel: UILabel!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setAppearance()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        alertLabel.isHidden = true
        alertLabel.textColor = .red
        alertLabel.text = ""
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleTap))
        view.addGestureRecognizer(tapGesture)
    }
    
    @objc func handleTap(){
        view.endEditing(true)
    }
    
    @IBAction func restartPasswordFunc(_ sender: Any) {
        alertLabel.isHidden = false

        if emailTextfield.text != ""
        {
            Auth.auth().sendPasswordReset(withEmail: emailTextfield.text!)
            
            alertLabel.text = "Şifre sıfırlama maili gönderildi."
            alertLabel.textColor = .green
            
            UIView.animate(withDuration: 0.01, animations:{
                self.alertLabel.bounds.size.width -= 10
            }, completion: { _ in
                UIView.animate(withDuration: 0.01) {
                    self.alertLabel.bounds.size.width += 10
                }
            })

            self.dismiss(animated: true)
        }
        else
        {
            UIView.animate(withDuration: 0.01, animations:{
                self.alertLabel.text = "Lütfen email giriniz!"
                self.alertLabel.bounds.size.width -= 10
            }, completion: { _ in
                UIView.animate(withDuration: 0.01) {
                    self.alertLabel.bounds.size.width += 10
                }
            })
        }
    }
    
    func setAppearance()
    {
        textFieldLeftImage(imageName: "emailIcon", textField: emailTextfield)
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
