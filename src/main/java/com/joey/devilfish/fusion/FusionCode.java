package com.joey.devilfish.fusion;

/**
 * 常量
 * Date: 2017/5/13
 *
 * @author xusheng
 */

public class FusionCode {
    public static final String sEmptyString = "";

    /**
     * 文件缓存路径
     */
    public interface FILE_CONSTANT {

        String FILE_CONSTANT_FILE_FOLDER = "devilfish";

        String CACHE_FILE_SUFFIX = ".txt";
    }

    // 国际化
    public interface LocalState {

        int LOCAL_STATE_CHINESE = 0;

        int LOCAL_STATE_ENGLISH = 1;
    }

    public interface OrderStatus {

        // 下单成功
        String ORDER_STATUS_CHECKOUT_SUCCESS = "0";

        // 快递员接单
        String ORDER_STATUS_TAKEN = "1";

        // 取件中
        String ORDER_STATUS_IN_PIECE = "2";

        // 在路上
        String ORDER_STATUS_ON_ROAD = "3";

        // 订单完成
        String ORDER_STATUS_FINISHED = "4";

        // 全部订单
        String ORDER_STATUS_ALL = "5";

        // 所有未完成的订单
        String ORDER_STATUS_UNFINISHED = "6";

        // 反馈
        String ORDER_STATUS_FEEDBACK = "8";

        // 用户取消
        String ORDER_STATUS_CANCELLED = "9";
    }

    public interface PageConstant {

        // 初始从1页开始
        int INIT_PAGE = 1;
        // 每页请求数目
        int PAGE_SIZE = 10;

        int PAGE_SIZE_FOR_HOME = 9;
    }

    public interface FoodPickupState {

        // 未取件
        String STATE_NOT_PICKUP = "0";

        // 已取件
        String STATE_ALREADY_PICKUP = "1";
    }

    public interface OrderUrgent {

        // 未催单
        String ORDER_URGENT_NOT = "0";

        // 催单
        String ORDER_URGENT = "1";
    }

    public interface FavoriteState {

        // 未收藏
        int FAVORITE_STATE_NOT_FAVORITE = 0;

        // 已收藏
        int FAVORITE_STATE_FAVORITE = 1;
    }

    public interface InRange {
        // 不在配送范围
        int NOT_IN_RANGE = 0;

        // 在配送范围
        int IN_RANGE = 1;
    }

    public interface FoodSpec {

        String HAS_SPEC = "1";

        String NOT_HAS_SPEC = "0";
    }

    public interface DefaultAddress {

        String IS_NOT_DEFAULT = "0";

        String IS_DEFAULT = "1";
    }

    public interface Sex {

        // 女士
        String FEMALE = "0";

        // 男士
        String MALE = "1";
    }

    public interface PayMethod {

        // visa
        String PAY_METHOD_VISA = "0";

        // paypal
        String PAY_METHOD_PAYPAL = "1";

        // balance
        String PAY_METHOD_BALANCE = "2";

        // 信用卡支付
        String PAY_METHOD_CREDIT_CARD = "3";

        // 现金支付
        String PAY_METHOD_CASH = "4";

        // 饭点支付
        String PAY_REWARD_POINT = "5";
    }

    public interface PayStatus {
        // 已支付
        String PAY_STATUS_PAIED = "1";

        // 未支付
        String PAY_STATUS_UNPAIED = "0";
    }

    public interface WorkStatus {

        // 上班
        String WORK_STATUS_ONWORK = "1";

        // 下班
        String WORK_STATUS_OFFWORK = "0";
    }

    public interface BannerType {

        // 0:首页banner 1:手机欢迎页 2：手机引导页
        String BANNER_TYPE_HOME = "0";

        String BANNER_TYPE_WELCOME = "1";

        String BANNER_TYPE_SLIDE = "2";
    }

    public interface MoneyLimit {

        String MONEY_LIMIT_YES = "1";

        String MONEY_LIMIT_NO = "0";
    }
}
