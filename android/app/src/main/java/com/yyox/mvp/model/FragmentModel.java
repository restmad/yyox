package com.yyox.mvp.model;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BaseModel;
import com.yyox.mvp.contract.FragmentContract;
import com.yyox.mvp.model.api.cache.CacheManager;
import com.yyox.mvp.model.api.service.ServiceManager;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.MessageItem;
import com.yyox.mvp.model.entity.Question;
import com.yyox.mvp.model.entity.Site;
import com.yyox.mvp.model.entity.UserDetail;
import com.yyox.mvp.model.entity.Warehouse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by dadaniu on 2017-01-24.
 */
@ActivityScope
public class FragmentModel extends BaseModel<ServiceManager,CacheManager> implements FragmentContract.Model {

    @Inject
    public FragmentModel(ServiceManager serviceManager, CacheManager cacheManager) {
        super(serviceManager, cacheManager);
    }

    @Override
    public Observable<BaseJson<MessageItem>> getMessages(String date, int pageNumber) {
        return mServiceManager.getCommonService().getMessages(date,pageNumber);
    }

    @Override
    public Observable<BaseJson<UserDetail>> getUserDetail() {
        return mServiceManager.getUserService().getUserDetail();
    }

    @Override
    public Observable<List<Question>> getQuestions(String value) {
        List<Question> questions = new ArrayList<Question>();

        Question questionObj0 = new Question();

        String question1 = "1.邮客与其他转运公司相比的优势在哪里？";
        String answer1 = "依托强大的清关和仓库信息化管理能力，邮客主打稳定快速服务，为客户提供高性价比的服务；而且邮客有诸多的优惠客户政策，最大效应地让利给客户。更着重于售后服务与运输服务细节。 另外，邮客在全球各地都有自己的物流服务，能够帮助用户实现全球海淘。";
        Question questionObj1 = new Question(question1,answer1);

        String question2 = "2.邮客海外是你们自己的仓库吗？";
        String answer2 = "是的，为保证服务质量，邮客海淘所有的海外仓库都是自建仓库。";
        Question questionObj2 = new Question(question2,answer2);

        String question3 = "3.转运物品有何限制？可以运奢侈品等贵重物品吗？";
        String answer3 = "转运限制见禁运物品，另外不定期根据各国海关以及航空要求会有调整，请关注运费服务页和公告。一线奢侈品等不承运，其他贵重物品请购买保险，并注意承运物品价格上限。如您不清楚您的物品是否可转运，请先行联系客服。切勿自行想象。";
        Question questionObj3 = new Question(question3,answer3);

        String question4 = "4. 在邮客充值后金额有使用期限吗？充值金额可以抵扣运费吗？";
        String answer4 = "邮客不设定充值金额使用期限；充值金额可直接抵扣运费及其附加费、增值服务费、补缴税金等。";
        Question questionObj4 = new Question(question4,answer4);

        String question5 = "5. 邮客转运超出承诺时效如何处理？";
        String answer5 = "邮客各线路转运时效请参考运费服务；如遇到我们操作问题延误时间，邮客会给出相应补偿公告。";
        Question questionObj5 = new Question(question5,answer5);

        questions.add(questionObj0);
        questions.add(questionObj1);
        questions.add(questionObj2);
        questions.add(questionObj3);
        questions.add(questionObj4);
        questions.add(questionObj5);

        return Observable.just(questions);
    }

    @Override
    public Observable<List<Site>> getSites() {
        List<Site> sites = new ArrayList<Site>();

        Site siteObj1 = new Site();
        siteObj1.setId(7);
        siteObj1.setName("美国亚马逊");
        siteObj1.setDetail("美国亚马逊美国亚马逊");
        siteObj1.setLogo("");
        siteObj1.setUrl("https://www.amazon.com/");
        siteObj1.setStatus(1);

        Site siteObj2 = new Site();
        siteObj2.setId(8);
        siteObj2.setName("日本亚马逊");
        siteObj2.setDetail("日本亚马逊日本亚马逊");
        siteObj2.setLogo("");
        siteObj2.setUrl("https://www.amazon.co.jp/");
        siteObj2.setStatus(1);

        Site siteObj3 = new Site();
        siteObj3.setId(9);
        siteObj3.setName("德国亚马逊");
        siteObj3.setDetail("德国亚马逊德国亚马逊");
        siteObj3.setLogo("");
        siteObj3.setUrl("https://www.amazon.de/");
        siteObj3.setStatus(1);

        Site siteObj4 = new Site();
        siteObj4.setId(10);
        siteObj4.setName("6pm");
        siteObj4.setDetail("6pm6pm");
        siteObj4.setLogo("");
        siteObj4.setUrl("http://www.6pm.com/");
        siteObj4.setStatus(1);

        Site siteObj5 = new Site();
        siteObj5.setId(11);
        siteObj5.setName("Drugstore");
        siteObj5.setDetail("DrugstoreDrugstore");
        siteObj5.setLogo("");
        siteObj5.setUrl("https://www.walgreens.com/");
        siteObj5.setStatus(1);

        Site siteObj6 = new Site();
        siteObj6.setId(12);
        siteObj6.setName("楽天市場");
        siteObj6.setDetail("楽天市場（rakuten）");
        siteObj6.setLogo("");
        siteObj6.setUrl("http://global.rakuten.com/zh-cn/");
        siteObj6.setStatus(1);

        sites.add(siteObj1);
        sites.add(siteObj2);
        sites.add(siteObj3);
        sites.add(siteObj4);
        sites.add(siteObj5);
        sites.add(siteObj6);

        return Observable.just(sites);
    }

    @Override
    public Observable<List<Site>> getWebs() {
        List<Site> sites = new ArrayList<Site>();

        Site siteObj1 = new Site();
        siteObj1.setId(1);
        siteObj1.setName("RebatesMe");
        siteObj1.setDetail("RebatesMe RebatesMe");
        siteObj1.setLogo("");
        siteObj1.setUrl("http://www.rebatesme.com/");
        siteObj1.setStatus(1);

        Site siteObj2 = new Site();
        siteObj2.setId(2);
        siteObj2.setName("55海淘");
        siteObj2.setDetail("55海淘 55海淘");
        siteObj2.setLogo("");
        siteObj2.setUrl("http://www.55haitao.com/");
        siteObj2.setStatus(1);

        Site siteObj3 = new Site();
        siteObj3.setId(3);
        siteObj3.setName("LetsEbuy");
        siteObj3.setDetail("LetsEbuy LetsEbuy");
        siteObj3.setLogo("");
        siteObj3.setUrl("http://www.letsebuy.com/");
        siteObj3.setStatus(1);

        Site siteObj4 = new Site();
        siteObj4.setId(4);
        siteObj4.setName("什么值得买");
        siteObj4.setDetail("什么值得买 什么值得买");
        siteObj4.setLogo("");
        siteObj4.setUrl("http://www.smzdm.com/");
        siteObj4.setStatus(1);

        Site siteObj5 = new Site();
        siteObj5.setId(5);
        siteObj5.setName("豆瓣东西");
        siteObj5.setDetail("豆瓣东西 豆瓣东西");
        siteObj5.setLogo("");
        siteObj5.setUrl("https://dongxi.douban.com/haitao/");
        siteObj5.setStatus(1);

        Site siteObj6 = new Site();
        siteObj6.setId(6);
        siteObj6.setName("哪里最便宜");
        siteObj6.setDetail("哪里最便宜 哪里最便宜");
        siteObj6.setLogo("");
        siteObj6.setUrl("http://www.nlzpy.com/");
        siteObj6.setStatus(1);

        sites.add(siteObj1);
        sites.add(siteObj2);
        sites.add(siteObj3);
        sites.add(siteObj4);
        sites.add(siteObj5);
        sites.add(siteObj6);

        return Observable.just(sites);
    }


}
