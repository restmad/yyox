//
//  YJWelcomeViewController.swift
//  yyox
//
//  Created by ddn on 2017/5/27.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

import UIKit

class YJWelcomeViewController: UIViewController {
	
	fileprivate let backgroundImageView = UIImageView()
	
	fileprivate let timeLabel = UILabel()
	
	fileprivate let skipBtn = UIButton()
	
	fileprivate var timer: Timer?
	
	fileprivate var currentTime = 2
	
	fileprivate let installed = YJEnterControllerManager.isInstalled()
	
	override func viewDidLoad() {
		super.viewDidLoad()
		
		setupUI()
	}
	
	override func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews()
		
		backgroundImageView.frame = view.bounds
		timeLabel.frame = CGRect(x: view.bounds.width - 60 - 20, y: 30, width: 60, height: 20)
		skipBtn.frame = CGRect(x: (view.bounds.width-160)/2, y: view.bounds.height*0.63, width: 160, height: 48)
	}
	
	override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(animated)
		if !installed {
			stopTimer()
		} else {
			startTimer()
		}
	}
	
	override func viewDidDisappear(_ animated: Bool) {
		super.viewDidDisappear(animated)
		stopTimer()
	}
}

extension YJWelcomeViewController {
	fileprivate func setupUI() {
		
		view.addSubview(backgroundImageView)
		view.addSubview(timeLabel)
		view.addSubview(skipBtn)
		
		timeLabel.textAlignment = .center
		timeLabel.textColor = .white
		timeLabel.backgroundColor = UIColor(white: 0.3, alpha: 0.5)
		timeLabel.font = UIFont.systemFont(ofSize: 14)
		timeLabel.isHidden = true
		
		backgroundImageView.image = UIImage(named: "welcome")
		
		skipBtn.setTitle("马上开启", for: .normal)
		skipBtn.setTitleColor(.white, for: .normal)
		skipBtn.layer.masksToBounds = true
		skipBtn.layer.cornerRadius = 10
		skipBtn.layer.borderColor = UIColor.white.cgColor
		skipBtn.layer.borderWidth = 1
		skipBtn.addTarget(self, action: #selector(clickOnSkip), for: .touchUpInside)
		skipBtn.isHidden = installed
	}
	
	@objc func clickOnSkip() {
		stopTimer()
		YJEnterControllerManager.switchEnterVC(from: self, animation: true)
	}
	
	fileprivate func startTimer() {
		stopTimer()
		skipBtn.isHidden = true
		timeLabel.isHidden = false
		currentTime = 3
		followTimer()
		timer = Timer(timeInterval: 1, target: self, selector: #selector(followTimer), userInfo: nil, repeats: true)
		RunLoop.current.add(timer!, forMode: .commonModes)
	}
	
	fileprivate func stopTimer() {
		timer?.invalidate()
		timer = nil
		timeLabel.isHidden = true
	}
	
	@objc fileprivate func followTimer() {
		currentTime -= 1
		if currentTime == 0 {
			clickOnSkip()
		} else {
			timeLabel.text = "\(currentTime) s"
		}
	}
}
