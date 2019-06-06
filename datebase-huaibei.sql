/*
 Navicat Premium Data Transfer

 Source Server         : 139.198.2.158_15439
 Source Server Type    : PostgreSQL
 Source Server Version : 100001
 Source Host           : 139.198.2.158:15439
 Source Catalog        : road_pricing
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 100001
 File Encoding         : 65001

 Date: 10/04/2018 16:14:50
*/

-- ----------------------------
-- 表空间创建
-- ----------------------------



-- ----------------------------
-- Sequence structure for t_account_balance_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_account_balance_id_seq";
CREATE SEQUENCE "public"."t_account_balance_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_account_summary_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_account_summary_id_seq";
CREATE SEQUENCE "public"."t_account_summary_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_arrears_record_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_arrears_record_id_seq";
CREATE SEQUENCE "public"."t_arrears_record_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_charge_rule_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_charge_rule_id_seq";
CREATE SEQUENCE "public"."t_charge_rule_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_collector_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_collector_id_seq";
CREATE SEQUENCE "public"."t_collector_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_config_id_seq";
CREATE SEQUENCE "public"."t_config_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_device_pos_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_device_pos_id_seq";
CREATE SEQUENCE "public"."t_device_pos_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_duty_record_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_duty_record_id_seq";
CREATE SEQUENCE "public"."t_duty_record_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_duty_statistics_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_duty_statistics_id_seq";
CREATE SEQUENCE "public"."t_duty_statistics_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_location_report_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_location_report_id_seq";
CREATE SEQUENCE "public"."t_location_report_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_member_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_member_id_seq";
CREATE SEQUENCE "public"."t_member_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_member_type_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_member_type_id_seq";
CREATE SEQUENCE "public"."t_member_type_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_order_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_order_id_seq";
CREATE SEQUENCE "public"."t_order_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_order_item_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_order_item_id_seq";
CREATE SEQUENCE "public"."t_order_item_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_parking_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_parking_id_seq";
CREATE SEQUENCE "public"."t_parking_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_parking_space_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_parking_space_id_seq";
CREATE SEQUENCE "public"."t_parking_space_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;



-- ----------------------------
-- Sequence structure for t_sub_parking_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_sub_parking_id_seq";
CREATE SEQUENCE "public"."t_sub_parking_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_user_id_seq";
CREATE SEQUENCE "public"."t_user_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_vehicle_entrance_record_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_vehicle_entrance_record_id_seq";
CREATE SEQUENCE "public"."t_vehicle_entrance_record_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for t_ver_picture_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."t_ver_picture_id_seq";
CREATE SEQUENCE "public"."t_ver_picture_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wx_car_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wx_car_id_seq";
CREATE SEQUENCE "public"."wx_car_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for wx_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."wx_user_id_seq";
CREATE SEQUENCE "public"."wx_user_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Table structure for t_account_balance
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_account_balance";
CREATE TABLE "public"."t_account_balance" (
   "id" int8 NOT NULL DEFAULT nextval('t_account_balance_id_seq'::regclass),
   "balance_date" date,
   "total_charge_amount" numeric,
   "total_member_preferential_amount" numeric,
   "total_real_charge_amount" numeric,
   "total_online_amount" numeric,
   "total_offline_amount" numeric,
   "total_arrears_charge_amount" numeric,
   "total_wipe_amount" numeric,
   "total_ali_charge_amount" numeric ,
   "total_wechat_charge_amount" numeric,
   "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."t_account_balance"."balance_date" IS '对账日期';
COMMENT ON COLUMN "public"."t_account_balance"."total_charge_amount" IS '应收总收入';
COMMENT ON COLUMN "public"."t_account_balance"."total_member_preferential_amount" IS '会员优惠金额总数';
COMMENT ON COLUMN "public"."t_account_balance"."total_online_amount" IS '线上支付总收入';
COMMENT ON COLUMN "public"."t_account_balance"."total_offline_amount" IS '线下现金总收入';
COMMENT ON COLUMN "public"."t_account_balance"."total_arrears_charge_amount" IS '欠费补缴总收入';
COMMENT ON COLUMN "public"."t_account_balance"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."t_account_balance" IS '对账记录表';

-- ----------------------------
-- Table structure for t_account_summary
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_account_summary";
CREATE TABLE "public"."t_account_summary" (
   "id" int8 NOT NULL DEFAULT nextval('t_account_summary_id_seq'::regclass),
   "collector_id" int8,
   "summary_date" date,
   "total_amount" numeric(7,2),
   "online_amount" numeric(7,2),
   "offline_amount" numeric(7,2),
   "summary_status" int4,
   "create_time" timestamp(6),
   "summary_time" timestamp(6),
   "remark" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."t_account_summary"."total_amount" IS '总收入';
COMMENT ON COLUMN "public"."t_account_summary"."online_amount" IS '线上收入';
COMMENT ON COLUMN "public"."t_account_summary"."offline_amount" IS '线下收入（现金收入）';
COMMENT ON COLUMN "public"."t_account_summary"."summary_status" IS '扎帐状态：0：带扎帐 1 线下扎帐2线上扎帐 3扎帐成功';
COMMENT ON COLUMN "public"."t_account_summary"."remark" IS '描述';
COMMENT ON COLUMN "public"."t_account_summary"."summary_date" IS '扎帐日期 yyyy-MM-dd';

-- ----------------------------
-- Table structure for t_arrears_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_arrears_record";
CREATE TABLE "public"."t_arrears_record" (
   "id" int8 NOT NULL DEFAULT nextval('t_arrears_record_id_seq'::regclass),
   "car_number" varchar(20) COLLATE "pg_catalog"."default",
   "arrears_amount" numeric(7,2),
   "vehicle_entrance_id" int8,
   "payment_status" int4,
   "create_time" timestamp(6),
   "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."t_arrears_record"."payment_status" IS '欠费状态 0：欠费未支付  1：已补交';
COMMENT ON COLUMN "public"."t_arrears_record"."arrears_amount" IS '欠费金额';
COMMENT ON COLUMN "public"."t_arrears_record"."car_number" IS '车牌号';
COMMENT ON COLUMN "public"."t_arrears_record"."vehicle_entrance_id" IS '停车记录ID';
-- ----------------------------
-- Table structure for t_charge_rule
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_charge_rule";
CREATE TABLE "public"."t_charge_rule" (
   "id" int8 NOT NULL DEFAULT nextval('t_charge_rule_id_seq'::regclass),
   "name" varchar(100) COLLATE "pg_catalog"."default",
   "start_time" time(6),
   "end_time" time(6),
   "description" varchar(500) COLLATE "pg_catalog"."default",
   "rule_type" int2,
   "rule_content" varchar(500) COLLATE "pg_catalog"."default",
   "create_time" timestamp(6),
   "max_charge_amount" numeric(6,2)
)
;
COMMENT ON COLUMN "public"."t_charge_rule"."start_time" IS '收费开始时间 HH:mm:ss';
COMMENT ON COLUMN "public"."t_charge_rule"."end_time" IS '收费截止时间 HH:mm:ss';
COMMENT ON COLUMN "public"."t_charge_rule"."rule_type" IS '规则类型 1：分时段收费 2：24小时循环收费 3：24小时自然日收费';
COMMENT ON COLUMN "public"."t_charge_rule"."max_charge_amount" IS '最大收费金额，此金额并非日最大收费金额，用于扩展，暂时未使用，日最大金额在规则json串中配置';

-- ----------------------------
-- Table structure for t_collector
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_collector";
CREATE TABLE "public"."t_collector" (
   "id" int8 NOT NULL DEFAULT nextval('t_collector_id_seq'::regclass),
   "name" varchar(100) COLLATE "pg_catalog"."default",
   "sex" bool,
   "phone" varchar(11) COLLATE "pg_catalog"."default",
   "address" varchar(500) COLLATE "pg_catalog"."default",
   "job_status" int4,
   "create_time" timestamp(6),
   "job_number" varchar(100) COLLATE "pg_catalog"."default",
   "password" varchar(50) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."t_collector"."job_status" IS '工作状态 0：离职 1：在职';
COMMENT ON COLUMN "public"."t_collector"."job_number" IS '工号';


-- ----------------------------
-- Table structure for t_device_pos
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_device_pos";
CREATE TABLE "public"."t_device_pos" (
   "id" int8 NOT NULL DEFAULT nextval('t_device_pos_id_seq'::regclass),
   "device_id" varchar(100) COLLATE "pg_catalog"."default",
   "bluetooth_id" VARCHAR(100) COLLATE "pg_catalog"."default",
   "sub_parking_id" int8,
   "device_type" int4 DEFAULT 0,
   "remark" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."t_device_pos"."sub_parking_id" IS '路段Id，0 标识适合所有路段';
COMMENT ON COLUMN "public"."t_device_pos"."device_type" IS '设备类型 0 ：通用设备  1：P990 pos机';


-- ----------------------------
-- Table structure for t_duty_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_duty_record";
CREATE TABLE "public"."t_duty_record" (
   "id" int8 NOT NULL DEFAULT nextval('t_duty_record_id_seq'::regclass),
   "collector_id" int8,
   "sub_parking_id" int8,
   "login_time" timestamp(6),
   "location" varchar(100) COLLATE "pg_catalog"."default",
   "device_id" varchar(50) COLLATE "pg_catalog"."default",
   "ip" varchar(20) COLLATE "pg_catalog"."default",
   "logout_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."t_duty_record"."logout_time" IS '登出时间,如果没有记录默认收费规则下班时间计算';


-- ----------------------------
-- Table structure for t_duty_statistics
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_duty_statistics";
CREATE TABLE "public"."t_duty_statistics" (
   "id" int8 NOT NULL DEFAULT nextval('t_duty_statistics_id_seq'::regclass),
   "collector_id" int8,
   "sub_parking_id" int8,
   "duty_date" date,
   "latest_on_duty_time" timestamp(6),
   "latest_off_duty_time" timestamp(6),
   "online_total_time" numeric(3,1),
   "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."t_duty_statistics"."duty_date" IS '执勤日期';
COMMENT ON COLUMN "public"."t_duty_statistics"."latest_on_duty_time" IS '最近签到（登入）时间';
COMMENT ON COLUMN "public"."t_duty_statistics"."latest_off_duty_time" IS '最近签退（登出）时间';
COMMENT ON COLUMN "public"."t_duty_statistics"."online_total_time" IS '在线总共时长 单位小时';

-- ----------------------------
-- Table structure for t_location_report
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_location_report";
CREATE TABLE "public"."t_location_report" (
   "id" int8 NOT NULL DEFAULT nextval('t_location_report_id_seq'::regclass),
   "device_id" varchar(100) COLLATE "pg_catalog"."default",
   "collector_id" int8,
   "location" varchar(100) COLLATE "pg_catalog"."default",
   "report_time" timestamp(6),
   "allow" bool,
   "distance" int4
)
;
COMMENT ON COLUMN "public"."t_location_report"."device_id" IS '设备ID';
COMMENT ON COLUMN "public"."t_location_report"."collector_id" IS '收费员ID';
COMMENT ON COLUMN "public"."t_location_report"."report_time" IS '上报时间 yyyy-MM-dd HH:mm:ss';
COMMENT ON COLUMN "public"."t_location_report".allow IS '是否在允许的考勤范围内';
COMMENT ON COLUMN "public"."t_location_report".distance IS '当前考勤距离';




-- ----------------------------
-- Table structure for t_member
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_member";
CREATE TABLE "public"."t_member" (
   "id" int8 NOT NULL DEFAULT nextval('t_member_id_seq'::regclass),
   "car_number" varchar(20) COLLATE "pg_catalog"."default",
   "sub_parking_id" int8,
   "member_type_id" int8,
   "start_date" date,
   "end_date" date,
   "create_time" timestamp(6),
   "creator" varchar(100) COLLATE "pg_catalog"."default",
   "charge_amount" numeric(6,2)
)
;
COMMENT ON COLUMN "public"."t_member"."car_number" IS '会员车牌号';
COMMENT ON COLUMN "public"."t_member"."sub_parking_id" IS '免费路段ID  0 表示所有路段均可';
COMMENT ON COLUMN "public"."t_member"."member_type_id" IS '会员类型ID';
COMMENT ON COLUMN "public"."t_member"."start_date" IS '会员开始日期 yyyy-MM-dd';
COMMENT ON COLUMN "public"."t_member"."end_date" IS '会员截止日期 yyyy-MM-dd';
COMMENT ON COLUMN "public"."t_member"."charge_amount" IS '入会金额 收费金额';



-- ----------------------------
-- Table structure for t_member_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_member_type";
CREATE TABLE "public"."t_member_type" (
   "id" int8 NOT NULL DEFAULT nextval('t_member_type_id_seq'::regclass),
   "name" varchar(100) COLLATE "pg_catalog"."default",
   "valid_time" int4,
   "remark" varchar(500) COLLATE "pg_catalog"."default",
   "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."t_member_type"."valid_time" IS '会员类型时间 : 单位 月 ';


-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_order";
CREATE TABLE "public"."t_order" (
   "id" int8 NOT NULL DEFAULT nextval('t_order_id_seq'::regclass),
   "order_type" int4,
   "collector_id" int8,
   "order_no" varchar(50) COLLATE "pg_catalog"."default",
   "pay_way" int4,
   "pay_amount" numeric(7,2),
   "pay_status" int4,
   "charge_rsp_info" varchar(1000) COLLATE "pg_catalog"."default",
   "callback_info" varchar(1000) COLLATE "pg_catalog"."default",
   "remark" varchar(500) COLLATE "pg_catalog"."default",
   "create_time" timestamp(6),
   "update_time" timestamp(6)
)
;

COMMENT ON COLUMN "public"."t_order"."order_type" IS '订单类型 1：缴费订单 2：欠费补缴订单 3：聚合订单 4：扎帐订单';
COMMENT ON COLUMN "public"."t_order"."collector_id" IS '收费员ID';
COMMENT ON COLUMN "public"."t_order"."pay_way" IS '支付方式 1：微信 2支付宝 3银行卡 4：现金';
COMMENT ON COLUMN "public"."t_order"."pay_amount" IS '支付金额';
COMMENT ON COLUMN "public"."t_order"."pay_status" IS '支付状态 1：待支付 2：支付成功 3：支付失败  4：支付取消 5:支付处理中 6：支付超时 7：交易异常';
COMMENT ON COLUMN "public"."t_order"."callback_info" IS '支付结果回调信息';
COMMENT ON COLUMN "public"."t_order"."charge_rsp_info" IS '预支付交易对象响应信息';

-- ----------------------------
-- Table structure for t_order_item
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_order_item";
CREATE TABLE "public"."t_order_item" (
   "id" int8 NOT NULL DEFAULT nextval('t_order_item_id_seq'::regclass),
   "order_id" int8,
   "biz_id" int8,
   "biz_type" int4,
   "pay_amount" numeric(7,2),
   "pay_status" int4,
   "update_time" timestamp(6),
   "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."t_order_item"."order_id" IS '订单id';
COMMENT ON COLUMN "public"."t_order_item"."biz_id" IS '业务id';
COMMENT ON COLUMN "public"."t_order_item"."biz_type" IS '业务类型： 1.停车缴费订单业务 2：欠费补缴业务 3 扎帐业务';
COMMENT ON COLUMN "public"."t_order_item"."pay_amount" IS '支付金额';
COMMENT ON COLUMN "public"."t_order_item"."pay_status" IS '支付状态';

-- ----------------------------
-- Table structure for t_parking
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_parking";
CREATE TABLE "public"."t_parking" (
   "id" int8 NOT NULL DEFAULT nextval('t_parking_id_seq'::regclass),
   "name" varchar(100) COLLATE "pg_catalog"."default",
   "total_parking_space" int4,
   "create_time" timestamp(6),
   "address" varchar(500) COLLATE "pg_catalog"."default",
   "charge_rule_id" int8
)
;
COMMENT ON COLUMN "public"."t_parking"."total_parking_space" IS '总停车位数';
COMMENT ON COLUMN "public"."t_parking"."charge_rule_id" IS '收费规则ID';

-- ----------------------------
-- Table structure for t_parking_space
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_parking_space";
CREATE TABLE "public"."t_parking_space" (
   "id" int8 NOT NULL DEFAULT nextval('t_parking_space_id_seq'::regclass),
   "code" varchar(32) COLLATE "pg_catalog"."default",
   "geo_sensor_code" varchar(32) COLLATE "pg_catalog"."default",
   "sub_parking_id" int8,
   "parking_id" int8,
   "create_time" timestamp(6),
   "version_no" int8 NOT NULL DEFAULT 1
)
;
COMMENT ON COLUMN "public"."t_parking_space"."geo_sensor_code" IS '地磁编码 备用';
COMMENT ON COLUMN "public"."t_parking_space"."code" IS '车位号（车位编码）';
COMMENT ON COLUMN "public"."t_parking_space"."sub_parking_id" IS '所属路段ID';
COMMENT ON COLUMN "public"."t_parking_space"."parking_id" IS '所属停车场ID 冗余';
COMMENT ON COLUMN "public"."t_parking_space"."version_no" IS '乐观锁 标识';


-- ----------------------------
-- Table structure for t_sub_parking
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_sub_parking";
CREATE TABLE "public"."t_sub_parking" (
   "id" int8 NOT NULL DEFAULT nextval('t_sub_parking_id_seq'::regclass),
   "parking_id" int8,
   "name" varchar(100) COLLATE "pg_catalog"."default",
   "code" varchar(32) COLLATE "pg_catalog"."default",
   "geo_location" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
   "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."t_sub_parking"."parking_id" IS '所属停车场ID';
COMMENT ON COLUMN "public"."t_sub_parking"."code" IS '路段编号';
COMMENT ON COLUMN "public"."t_sub_parking"."geo_location" IS '路段地理左边 纬度,经度  ';

-- ----------------------------
-- Table structure for t_sys_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_sys_config";
CREATE TABLE "public"."t_sys_config" (
   "id" int4 NOT NULL DEFAULT nextval('t_config_id_seq'::regclass),
   "cfg_key" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
   "cfg_value" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
   "remark" varchar(200) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."t_sys_config"."cfg_value" IS '配置项值';
COMMENT ON COLUMN "public"."t_sys_config"."remark" IS '描述';
COMMENT ON TABLE "public"."t_sys_config" IS '配置项管理';

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_user";
CREATE TABLE "public"."t_user" (
   "id" int8 NOT NULL DEFAULT nextval('t_user_id_seq'::regclass),
   "user_name" varchar(100) COLLATE "pg_catalog"."default",
   "login_name" varchar(100) COLLATE "pg_catalog"."default",
   "user_type" int4,
   "password" varchar(32) COLLATE "pg_catalog"."default",
   "sex" bool,
   "phone" varchar(11) COLLATE "pg_catalog"."default",
   "salt" char(4) COLLATE "pg_catalog"."default",
   "delete" bool,
   "create_time" timestamp(6),
   "update_time" timestamp(6)

)
;
COMMENT ON COLUMN "public"."t_user"."user_name" IS '用户姓名';
COMMENT ON COLUMN "public"."t_user"."login_name" IS '登录名';
COMMENT ON COLUMN "public"."t_user"."password" IS '登录密码';
COMMENT ON COLUMN "public"."t_user"."user_type" IS '用户类型 ：1:管理员 2：财务人员...';
COMMENT ON COLUMN "public"."t_user"."password" IS '登录密码';


-- ----------------------------
-- Table structure for t_vehicle_entrance_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_vehicle_entrance_record";
CREATE TABLE "public"."t_vehicle_entrance_record" (
   "id" int8 NOT NULL DEFAULT nextval('t_vehicle_entrance_record_id_seq'::regclass),
   "enter_time" timestamp(6),
   "enter_collector_id" int8,
   "exit_time" timestamp(6),
   "exit_collector_id" int8,
   "parking_id" int8,
   "sub_parking_id" int8,
   "parking_space_id" int8,
   "car_number" varchar(20) COLLATE "pg_catalog"."default",
   "charge_amount" numeric(5,2),
   "real_charge_amount" numeric(5,2),
   "biz_status" int4,
   "member_id" int8,
   "version_no" int8,
   "create_time" timestamp(6),
   "update_time" timestamp(6)

)
;
COMMENT ON COLUMN "public"."t_vehicle_entrance_record"."enter_time" IS '入场时间';
COMMENT ON COLUMN "public"."t_vehicle_entrance_record"."exit_time" IS '出场时间';
COMMENT ON COLUMN "public"."t_vehicle_entrance_record"."parking_id" IS '停车场Id';
COMMENT ON COLUMN "public"."t_vehicle_entrance_record"."sub_parking_id" IS '路段Id';
COMMENT ON COLUMN "public"."t_vehicle_entrance_record"."parking_space_id" IS '车位ID';
COMMENT ON COLUMN "public"."t_vehicle_entrance_record"."car_number" IS '车牌号';
COMMENT ON COLUMN "public"."t_vehicle_entrance_record"."biz_status" IS '业务状态 0：车辆驶入 1：驶出支付中 2：缴费驶出 3：欠费离场 4：欠费补缴 5：人工干预';
COMMENT ON COLUMN "public"."t_vehicle_entrance_record"."member_id" IS '会员ID';
COMMENT ON COLUMN "public"."t_vehicle_entrance_record"."version_no" IS '车位乐观锁标识快照';

-- ----------------------------
-- Table structure for t_ver_picture
-- ----------------------------
DROP TABLE IF EXISTS "public"."t_ver_picture";
CREATE TABLE "public"."t_ver_picture" (
   "id" int8 NOT NULL DEFAULT nextval('t_ver_picture_id_seq'::regclass),
   "vehicle_entrance_record_id" int8 NOT NULL,
   "picturl_url" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
   "order_num" int4 NOT NULL
)
;
COMMENT ON COLUMN "public"."t_ver_picture"."vehicle_entrance_record_id" IS '出入场记录ID';
COMMENT ON COLUMN "public"."t_ver_picture"."picturl_url" IS '图片URL地址 相对地址';
COMMENT ON COLUMN "public"."t_ver_picture"."order_num" IS '图片上传顺序';
COMMENT ON TABLE "public"."t_ver_picture" IS '出入场记录与图片关联表';

-- ----------------------------
-- Table structure for wx_car
-- ----------------------------
DROP TABLE IF EXISTS "public"."wx_car";
CREATE TABLE "public"."wx_car" (
   "id" int4 NOT NULL DEFAULT nextval('wx_car_id_seq'::regclass),
   "user_id" int4 NOT NULL,
   "number1" varchar(3) COLLATE "pg_catalog"."default" NOT NULL,
   "number2" varchar(7) COLLATE "pg_catalog"."default" NOT NULL,
   "status" int2 NOT NULL DEFAULT 0,
   "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Table structure for wx_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."wx_user";
CREATE TABLE "public"."wx_user" (
   "id" int4 NOT NULL DEFAULT nextval('wx_user_id_seq'::regclass),
   "open_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
   "nickname" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
   "header_img" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
   "subscribe" int2 NOT NULL DEFAULT 0,
   "create_time" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
   "create_ip" varchar(16) COLLATE "pg_catalog"."default",
   "update_time" timestamp(6)
)
;
-- ----------------------------
-- 基础数据
-- ----------------------------
INSERT INTO "public"."t_sys_config" VALUES (1, 'REFUSE_LOGIN_HOUR_TIME', '22', '拒绝登录APP时间[0-24] 建议配置值在收费截止时间后的第一个小时-22之间的某个整数值');
INSERT INTO "public"."t_sys_config" VALUES (2, 'REFUSE_LOGIN_RANGE', '300', '拒绝登录APP单位：米；获取APP登录的坐标地点距离路段所在位置距离超过该距离，拒绝登录');
INSERT INTO "public"."t_sys_config" VALUES (3, 'LOGIN_IS_VERIFY_RANGE', 'true', '登录是否要求距离范围校验true/false');
INSERT INTO "public"."t_sys_config" VALUES (4, 'CAR_FREE_EXIT_2ND_CONFIRM', 'false', '车辆在免费时段驶出true/false');
INSERT INTO "public"."t_sys_config" VALUES (5, 'REPORT_TIMES', '15', '考勤GPS发送频率，单位：分钟');


-- ----------------------------
-- Alter sequences owned by 设置基础数据 变更序列初始值
-- ----------------------------
ALTER SEQUENCE "t_config_id_seq"  OWNED BY "public"."t_sys_config"."id";
SELECT setval('"public"."t_config_id_seq"', 6, true);

-- ----------------------------
-- 约束设置
-- ----------------------------
-- ----------------------------
-- Primary Key 主键设置
-- ----------------------------
ALTER TABLE "public"."t_account_balance" ADD CONSTRAINT "t_account_balance_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table t_account_summary
-- ----------------------------
CREATE UNIQUE INDEX "t_account_summary_collector_id_summary_date_uindex" ON "public"."t_account_summary" USING btree (
   "collector_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
   "summary_date" "pg_catalog"."date_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table t_account_summary
-- ----------------------------
ALTER TABLE "public"."t_account_summary" ADD CONSTRAINT "pk_t_account_summary" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_arrears_record
-- ----------------------------
ALTER TABLE "public"."t_arrears_record" ADD CONSTRAINT "pk_t_arrears_record" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_charge_rule
-- ----------------------------
ALTER TABLE "public"."t_charge_rule" ADD CONSTRAINT "pk_t_charge_rule" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_collector
-- ----------------------------
ALTER TABLE "public"."t_collector" ADD CONSTRAINT "pk_t_collector" PRIMARY KEY ("id");


-- ----------------------------
-- Primary Key structure for table t_device_pos
-- ----------------------------
ALTER TABLE "public"."t_device_pos" ADD CONSTRAINT "pk_t_device_pos" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_duty_record
-- ----------------------------
ALTER TABLE "public"."t_duty_record" ADD CONSTRAINT "pk_t_duty_record" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_duty_statistics
-- ----------------------------
ALTER TABLE "public"."t_duty_statistics" ADD CONSTRAINT "pk_t_duty_statistics" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_location_report
-- ----------------------------
ALTER TABLE "public"."t_location_report" ADD CONSTRAINT "pk_t_location_report" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_member
-- ----------------------------
ALTER TABLE "public"."t_member" ADD CONSTRAINT "pk_t_member" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_member_type
-- ----------------------------
ALTER TABLE "public"."t_member_type" ADD CONSTRAINT "pk_t_member_type" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_order
-- ----------------------------
ALTER TABLE "public"."t_order" ADD CONSTRAINT "pk_t_order" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_order_item
-- ----------------------------
ALTER TABLE "public"."t_order_item" ADD CONSTRAINT "t_order_item_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_parking
-- ----------------------------
ALTER TABLE "public"."t_parking" ADD CONSTRAINT "pk_t_parking" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_parking_space
-- ----------------------------
ALTER TABLE "public"."t_parking_space" ADD CONSTRAINT "pk_t_parking_space" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_sub_parking
-- ----------------------------
ALTER TABLE "public"."t_sub_parking" ADD CONSTRAINT "pk_t_sub_parking" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_sys_config
-- ----------------------------
ALTER TABLE "public"."t_sys_config" ADD CONSTRAINT "t_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table t_user
-- ----------------------------
ALTER TABLE "public"."t_user" ADD CONSTRAINT "pk_t_user" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table t_vehicle_entrance_record
-- ----------------------------
CREATE UNIQUE INDEX "t_vehicle_entrance_record_parking_space_id_version_no_uindex" ON "public"."t_vehicle_entrance_record" USING btree (
   "parking_space_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
   "version_no" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table t_vehicle_entrance_record
-- ----------------------------
ALTER TABLE "public"."t_vehicle_entrance_record" ADD CONSTRAINT "pk_t_vehicle_entrance_record" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table t_ver_picture
-- ----------------------------
CREATE UNIQUE INDEX "t_ver_picture_vehicle_entrance_record_id_order_num_uindex" ON "public"."t_ver_picture" USING btree (
   "vehicle_entrance_record_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
   "order_num" "pg_catalog"."int4_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table t_ver_picture
-- ----------------------------
ALTER TABLE "public"."t_ver_picture" ADD CONSTRAINT "pk_t_ver_picture" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wx_car
-- ----------------------------
ALTER TABLE "public"."wx_car" ADD CONSTRAINT "pk_wx_car" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wx_user
-- ----------------------------
ALTER TABLE "public"."wx_user" ADD CONSTRAINT "pk_wx_user" PRIMARY KEY ("id");
