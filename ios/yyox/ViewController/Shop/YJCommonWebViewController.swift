//
//  YJCommonWebViewController.swift
//  YJShopModule
//
//  Created by ddn on 2017/6/20.
//  Copyright © 2017年 CocoaPods. All rights reserved.
//

import UIKit
import WebKit
import YJWebViewHolder

public class YJCommonWebViewController: UIViewController {
	
	public lazy var webViewHolder: YJWebViewHolder = YJWebViewHolder(self)
	
	public var model: YJShopModel?
	
	override public func viewDidLoad() {
		super.viewDidLoad()
		
		view.addSubview(webViewHolder.webV)
		view.addSubview(webViewHolder.progressV)
		
		if let url = model?.url {
			webViewHolder.loadURL(url)
		}
	}
	
	override public func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews()
		
		webViewHolder.webV.frame = view.bounds
		webViewHolder.progressV.frame = CGRect(x: 0, y: topLayoutGuide.length, width: view.bounds.width, height: 3)
	}
}

extension YJCommonWebViewController: YJWebViewHolderDelegate {
	
}

