//
//  YJShopViewController.swift
//  YJShopModule
//
//  Created by ddn on 2017/6/20.
//  Copyright © 2017年 CocoaPods. All rights reserved.
//

import UIKit
import Alamofire
import Kingfisher
import SwiftyJSON

let BaseUrl = "http://app.yyox.com"

private class YJShopFooterView: UICollectionReusableView {
	
}

private class YJShopHeaderView: UICollectionReusableView {
	private let titleLabel: UILabel = UILabel()
	private let optBtn: UIButton = UIButton()
	
	private let topSpaceView = UIView()
	private let lineView = UIView()
	
	var clickOnAdd: (()->())?
	
	override init(frame: CGRect) {
		super.init(frame: frame)
		self.setup()
		backgroundColor = UIColor.white
	}
	
	required init?(coder aDecoder: NSCoder) {
		fatalError("init(coder:) has not been implemented")
	}
	
	private func setup() {
		titleLabel.font = UIFont.systemFont(ofSize: 14)
		titleLabel.textColor = UIColor(red: 51.0/255.0, green: 51.0/255.0, blue: 51.0/255.0, alpha: 1)
		
		optBtn.titleLabel?.font = UIFont.systemFont(ofSize: 13)
		optBtn.setTitle("添加", for: .normal)
		optBtn.setTitleColor(UIColor(red: 252.0/255.0, green: 155.0/255.0, blue: 49.0/255.0, alpha: 1), for: .normal)
		optBtn.sizeToFit()
		
		optBtn.addTarget(self, action: #selector(clickOnOpt), for: .touchUpInside)
		optBtn.isHidden = true
		
		topSpaceView.backgroundColor = UIColor(red: 102.0/255.0, green: 102.0/255.0, blue: 102.0/255.0, alpha: 1)
		lineView.backgroundColor = UIColor(red: 0.8, green: 0.8, blue: 0.8, alpha: 1)
		
		addSubview(titleLabel)
		addSubview(optBtn)
		
		addSubview(topSpaceView)
		addSubview(lineView)
	}
	
	func clickOnOpt() {
		if let clickOnAdd = clickOnAdd {
			clickOnAdd()
		}
	}
	
	fileprivate override func layoutSubviews() {
		super.layoutSubviews()
		
		titleLabel.frame.origin.x = 20;
		titleLabel.frame.origin.y = (frame.height - titleLabel.frame.height) * 0.5
		
		optBtn.frame.origin.x = frame.width - optBtn.frame.width - 10
		optBtn.frame.origin.y = (frame.height - optBtn.frame.height) * 0.5
		
		lineView.frame = CGRect(x: 0, y: frame.height - 0.3, width: frame.width, height: 0.3)
	}
	
	func setTitle(_ title: String?) {
		titleLabel.text = title
		titleLabel.sizeToFit()
	}
}

public struct YJShopModel {
	var icon: String?
	var name: String?
	var url: String?
}

private class YJShopCell: UICollectionViewCell {
	
	let imageView: UIImageView = UIImageView()
	
	let titleLabel: UILabel = UILabel()
	
	var model: YJShopModel? {
		didSet {
			titleLabel.text = model?.name
			if let icon = model?.icon {
				imageView.kf.setImage(with: URL(string: icon))
			}
		}
	}
	
	override init(frame: CGRect) {
		super.init(frame: frame)
		setup()
		backgroundColor = UIColor.white
	}
	
	required init?(coder aDecoder: NSCoder) {
		fatalError("init(coder:) has not been implemented")
	}
	
	private func setup() {
		imageView.contentMode = .scaleAspectFit
		
		titleLabel.font = UIFont.systemFont(ofSize: 13)
		titleLabel.textColor = UIColor(red: 102.0/255.0, green: 102.0/255.0, blue: 102.0/255.0, alpha: 1)
		titleLabel.textAlignment = .center
		
		contentView.addSubview(imageView)
		contentView.addSubview(titleLabel)
	}
	
	fileprivate override func layoutSubviews() {
		super.layoutSubviews()
		
		titleLabel.frame = CGRect(x: 0, y: frame.height - titleLabel.font.lineHeight - 20.0, width: frame.width, height: titleLabel.font.lineHeight)
		
		let height: CGFloat = titleLabel.frame.minY - 10.0 - 42.0 * 0.5
		imageView.frame = CGRect(x: 15, y: 42.0 * 0.5, width: frame.width - 30, height: height)
	}
}

public class YJShopViewController: UIViewController {
	
	private lazy var activityIndicator: UIActivityIndicatorView = {
		let activityIndicator = UIActivityIndicatorView(activityIndicatorStyle: .gray)
		self.view.addSubview(activityIndicator)
		activityIndicator.hidesWhenStopped = true
		return activityIndicator
	}()
	
	private lazy var collectionView : UICollectionView = {
		let collectionView = UICollectionView(frame: self.view.bounds, collectionViewLayout: UICollectionViewFlowLayout())
		collectionView.delegate = self
		collectionView.dataSource = self
		collectionView.register(YJShopCell.self, forCellWithReuseIdentifier: "cell")
		collectionView.register(YJShopHeaderView.self, forSupplementaryViewOfKind: UICollectionElementKindSectionHeader, withReuseIdentifier: "header")
		collectionView.register(YJShopFooterView.self, forSupplementaryViewOfKind: UICollectionElementKindSectionFooter, withReuseIdentifier: "footer")
		collectionView.alwaysBounceVertical = true
		self.view.addSubview(collectionView)
		return collectionView
	}()
	
	fileprivate lazy var commonShops : [YJShopModel] = {
		var commonShops = [YJShopModel]()
		return commonShops
	}()
	
	fileprivate lazy var specShops : [YJShopModel] = {
		var specShops = [YJShopModel]()
		return specShops
	}()
	
	fileprivate weak var request: DataRequest?
	
	private lazy var localShopsFilePath: String = {
		let caches = NSSearchPathForDirectoriesInDomains(.cachesDirectory, .userDomainMask, true)[0]
		let shops = caches.appending("/shops")
		
		return shops
	}()
	
	public var baseUrl: String = BaseUrl
	
	override public func viewDidLoad() {
		super.viewDidLoad()
		collectionView.backgroundColor = UIColor(red: 229.0/255.0, green: 234.0/255.0, blue: 231.0/255.0, alpha: 1)
		collectionView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 64, right: 0)
		collectionView.scrollIndicatorInsets = collectionView.contentInset;
		view.backgroundColor = UIColor.white
		
		loadLocalShops()
		loadData()
	}
	
	private func loadLocalShops() {
		
		if !FileManager.default.fileExists(atPath: localShopsFilePath) { return }
		
		guard let localShopsData = try? Data(contentsOf: URL(fileURLWithPath: localShopsFilePath)) else { return }
		
		generateRealData(localShopsData)
		collectionView.reloadData()
	}
	
	private func generateRealData(_ data: Data) {
		let json = JSON(data)
		let common = json["data"]["common"]
		let spec = json["data"]["special"]
		
		for (_, subJson):(String, JSON) in common {
			let shop = YJShopModel(icon: self.baseUrl + subJson["icon"].stringValue, name: subJson["name"].stringValue, url: subJson["url"].stringValue)
			self.commonShops.append(shop)
		}
		for (_, subJson):(String, JSON) in spec {
			let shop = YJShopModel(icon: self.baseUrl + subJson["icon"].stringValue, name: subJson["name"].stringValue, url: subJson["url"].stringValue)
			self.specShops.append(shop)
		}
	}
	
	fileprivate func loadData() {
		activityIndicator.startAnimating()
		
		var params = [String: Any]()
		if let infoDic = Bundle.main.infoDictionary, let version = infoDic["CFBundleShortVersionString"] {
			params["version"] = version
		}
		
		request = Alamofire.request(baseUrl + "/app/server/haitao", method: .get, parameters: params).responseData(completionHandler: { (response: DataResponse<Data>) in
			self.activityIndicator.stopAnimating()
			if response.result.isSuccess {
				self.commonShops.removeAll(keepingCapacity: true)
				self.specShops.removeAll(keepingCapacity: true)
				if let result = response.value {
					self.generateRealData(result)
					
					try? FileManager.default.removeItem(atPath: self.localShopsFilePath)
					FileManager.default.createFile(atPath: self.localShopsFilePath, contents: result, attributes: nil)
				}
				self.collectionView.reloadData()
			}
		})
	}
	
	public override func viewDidAppear(_ animated: Bool) {
		super.viewDidAppear(animated)
		
		if request == nil || request?.task == nil {
			loadData()
		}
	}
	
	override public func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews()
		
		activityIndicator.center = CGPoint(x: view.bounds.width / 2, y: view.bounds.height / 2)
	}
}

extension YJShopViewController: UICollectionViewDelegate {
	
	public func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
		collectionView.deselectItem(at: indexPath, animated: true)
		
		var model: YJShopModel?
		switch indexPath.section {
		case 0:
			if commonShops.count > indexPath.row {
				model = commonShops[indexPath.row]
			}
			break;
		default:
			if specShops.count > indexPath.row {
				model = specShops[indexPath.row]
			}
			break
		}
		if let model = model {
			let webVc = YJCommonWebViewController()
			webVc.model = model
			webVc.title = model.name
			navigationController?.pushViewController(webVc, animated: true)
		}
	}
}

extension YJShopViewController: UICollectionViewDataSource {
	public func numberOfSections(in collectionView: UICollectionView) -> Int {
		return 2
	}
	
	public func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
		return section > 0 ? specShops.count : commonShops.count
	}
	
	public func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
		let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "cell", for: indexPath) as! YJShopCell
		if indexPath.section == 0 {
			cell.model = commonShops[indexPath.row]
		} else {
			cell.model = specShops[indexPath.row]
		}
		return cell
	}
	
	public func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
		if kind == UICollectionElementKindSectionHeader {
			let headerView = collectionView.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: "header", for: indexPath) as! YJShopHeaderView
			let title = indexPath.section > 0 ? "导购网站" : "常用海淘"
			headerView.setTitle(title)
			headerView.clickOnAdd = {
				print("click on add" + "\(indexPath.section)")
			}
			return headerView
		} else {
			let footer = collectionView.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: "footer", for: indexPath) as! YJShopFooterView
			footer.backgroundColor = UIColor(red: 229.0/255.0, green: 234.0/255.0, blue: 231.0/255.0, alpha: 1)
			
			return footer
		}
	}
}

extension YJShopViewController: UICollectionViewDelegateFlowLayout {
	public func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
		return 0
	}
	
	public func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
		return 0
	}
	
	public func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
		let w = collectionView.bounds.width / 3
		let h: CGFloat = (40.0 + 42.0 + 20.0 + 26.0 + 90.0) * 0.5
		
		let floorW = floor(w)
		
		let mis = collectionView.bounds.width - floorW * 3
		
		if indexPath.item == 0 || indexPath.item == 2 {
			return CGSize(width: floorW + mis / 2, height: h)
		}
		
		return CGSize(width: floorW, height: h)
	}
	
	public func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
		return CGSize(width: collectionView.bounds.width, height: 50)
	}
	
	public func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForFooterInSection section: Int) -> CGSize {
		return CGSize(width: collectionView.bounds.width, height: 10)
	}
}
