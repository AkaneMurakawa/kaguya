/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : kaguya

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2019-12-19 18:18:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(11) NOT NULL COMMENT '分类id',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `order_id` int(2) NOT NULL DEFAULT '0' COMMENT '展示顺序，从0开始',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE KEY `uk_category_id` (`category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='分类';

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('1', '11676647343263744', '主页', '0');
INSERT INTO `category` VALUES ('2', '11676648115015680', 'Java', '1');
INSERT INTO `category` VALUES ('3', '11676648802881536', '设计模式', '2');
INSERT INTO `category` VALUES ('4', '11676650212167680', 'Spring', '3');
INSERT INTO `category` VALUES ('5', '11676651051028480', '数据结构', '4');
INSERT INTO `category` VALUES ('9', '13459556500770816', '留言', '5');
INSERT INTO `category` VALUES ('10', '13462484460507136', '读书', '6');

-- ----------------------------
-- Table structure for document
-- ----------------------------
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document` (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `document_id` bigint(11) NOT NULL COMMENT '文档id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` varchar(45) NOT NULL DEFAULT 'System' COMMENT '创建人',
  `update_user` varchar(45) DEFAULT NULL COMMENT '更新人',
  `content` text NOT NULL COMMENT '内容',
  `views` int(11) DEFAULT '0' COMMENT '浏览次数',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE KEY `uk_document_id` (`document_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文档基本属性';

-- ----------------------------
-- Records of document
-- ----------------------------
INSERT INTO `document` VALUES ('1', '11856480811618304', '2019-12-17 14:11:00', null, 'AkaneMurakwa', null, '```java\npublic static void main(String args[]) {\n	System.out.println(\"Hello World\");\n}\n```', '0');
INSERT INTO `document` VALUES ('2', '11861475942469632', '2019-12-17 14:15:58', null, 'AkaneMurakwa', null, '```\n数据类型	默认值\nbyte	0\nshort	0\nint	0\nlong	0L\nfloat	0.0f\ndouble	0.0d\nchar	\'u0000\'\nString (or any object)	null\nboolean	false\n```', '0');
INSERT INTO `document` VALUES ('3', '12146786274643968', '2019-12-17 18:59:23', null, 'AkaneMurakwa', null, 'if-else', '0');
INSERT INTO `document` VALUES ('4', '13467517826633728', '2019-12-18 16:51:25', null, 'AkaneMurakwa', null, '### 初步认识条件结构\n```\nif (...) {\n\n} else {\n\n}\n\nif (...) {\n\n} else if (...){\n\n} else {\n\n}\n```\n今天起得晚中午懒得去买菜就打电话给老公：亲爱的，你下班后去超市买10个鸡蛋回来，看到有卖西瓜的就买一个。老公说好的，等他回来时就看他手上就一个鸡蛋，我问：你怎么就买一个鸡蛋', '0');
INSERT INTO `document` VALUES ('5', '13470909777055744', '2019-12-18 16:54:47', null, 'AkaneMurakwa', null, '爱的飞行日记\n\n填    词：方文山 \n\n谱    曲：周杰伦\n\n赤道的边境     万里无云 天很清\n\n爱你的事情     说了千遍 有回音\n\n岸边的丘陵     崎岖不平 浪入侵\n\n我却很专心     分辨得出 你的声音\n\n用南极的冰     将爱结晶 我用心\n\n永不融化的是     爱你的这个决定\n\n透明 坚硬     升空对抗 重力反应\n\n逐渐渺小的风景     景景景景景色分明\n\n我加速引擎     抛开远方的黎明\n\n剩速度回应   向银河逼近\n\n我对着流星     祈祷时专心\n\n为爱飞行    脱离地心引力的热情\n\n找一颗星 只为了你命名     我用光年传递爱情\n\n为爱飞行    脱离地心引力的热情\n\n我在宇宙无重力的环境     为你降临\n\n赤道的边境   万里无云 天很清\n\n爱你的事情    说了千遍 有回音\n\n岸边的丘陵    崎岖不平 浪入侵\n\n我却很专心   分辨得出 你的声音\n\n用南极的冰    将爱结晶 我用心\n\n永不融化的是    爱你的这个决定\n\n透明（透明）坚硬（坚硬）     升空对抗 重力反应\n\n逐渐渺小的风景     景景景景景色分明\n\n我加速引擎     抛开远方的黎明\n\n剩速度回应     向银河逼近\n\n我对着流星    祈祷时专心\n\n为爱飞行     脱离地心引力的热情\n\n找一颗星     只为了你命名\n\n我用光年传递爱情    为爱飞行\n\n脱离地心引力的热情     我在宇宙无重力的环境\n\n为你降临', '0');
INSERT INTO `document` VALUES ('6', '13521233892085760', '2019-12-18 17:44:47', null, 'AkaneMurakwa', null, '三级目录测试', '0');
INSERT INTO `document` VALUES ('7', '13526443972628480', '2019-12-18 17:49:57', null, 'AkaneMurakwa', null, '# 欢迎使用 Markdown在线编辑器 MdEditor\n\n**Markdown是一种轻量级的「标记语言」**\n\n\n![markdown](https://www.mdeditor.com/images/logos/markdown.png \"markdown\")\n\n\nMarkdown是一种可以使用普通文本编辑器编写的标记语言，通过简单的标记语法，它可以使普通文本内容具有一定的格式。它允许人们使用易读易写的纯文本格式编写文档，然后转换成格式丰富的HTML页面，Markdown文件的后缀名便是“.md”\n\n\n## MdEditor是一个在线编辑Markdown文档的编辑器\n\n*MdEditor扩展了Markdown的功能（如表格、脚注、内嵌HTML等等），以使让Markdown转换成更多的格式，和更丰富的展示效果，这些功能原初的Markdown尚不具备。*\n\n> Markdown增强版中比较有名的有Markdown Extra、MultiMarkdown、 Maruku等。这些衍生版本要么基于工具，如~~Pandoc~~，Pandao；要么基于网站，如GitHub和Wikipedia，在语法上基本兼容，但在一些语法和渲染效果上有改动。\n\nMdEditor源于Pandao的JavaScript开源项目，开源地址[Editor.md](https://github.com/pandao/editor.md \"Editor.md\")，并在MIT开源协议的许可范围内进行了优化，以适应广大用户群体的需求。向优秀的markdown开源编辑器原作者Pandao致敬。\n\n\n![Pandao editor.md](https://pandao.github.io/editor.md/images/logos/editormd-logo-180x180.png \"Pandao editor.md\")\n\n\n\n## MdEditor的功能列表演示\n\n# 标题H1\n\n## 标题H2\n\n### 标题H3\n\n#### 标题H4\n\n##### 标题H5\n\n###### 标题H5\n\n### 字符效果和横线等\n----\n\n~~删除线~~ <s>删除线（开启识别HTML标签时）</s>\n\n*斜体字*      _斜体字_\n\n**粗体**  __粗体__\n\n***粗斜体*** ___粗斜体___\n\n上标：X<sub>2</sub>，下标：O<sup>2</sup>\n\n**缩写(同HTML的abbr标签)**\n> 即更长的单词或短语的缩写形式，前提是开启识别HTML标签时，已默认开启\n\nThe <abbr title=\"Hyper Text Markup Language\">HTML</abbr> specification is maintained by the <abbr title=\"World Wide Web Consortium\">W3C</abbr>.\n### 引用 Blockquotes\n\n> 引用文本 Blockquotes\n\n引用的行内混合 Blockquotes\n\n> 引用：如果想要插入空白换行`即<br />标签`，在插入处先键入两个以上的空格然后回车即可，[普通链接](https://www.mdeditor.com/)。\n\n### 锚点与链接 Links\n[普通链接](https://www.mdeditor.com/)\n[普通链接带标题](https://www.mdeditor.com/ \"普通链接带标题\")\n直接链接：<https://www.mdeditor.com>\n[锚点链接][anchor-id]\n[anchor-id]: https://www.mdeditor.com/\n[mailto:test.test@gmail.com](mailto:test.test@gmail.com)\nGFM a-tail link @pandao\n邮箱地址自动链接 test.test@gmail.com  www@vip.qq.com\n> @pandao\n\n### 多语言代码高亮 Codes\n\n#### 行内代码 Inline code\n\n\n执行命令：`npm install marked`\n\n#### 缩进风格\n\n即缩进四个空格，也做为实现类似 `<pre>` 预格式化文本 ( Preformatted Text ) 的功能。\n\n    <?php\n        echo \"Hello world!\";\n    ?>\n预格式化文本：\n\n    | First Header  | Second Header |\n    | ------------- | ------------- |\n    | Content Cell  | Content Cell  |\n    | Content Cell  | Content Cell  |\n\n#### JS代码\n```javascript\nfunction test() {\n	console.log(\"Hello world!\");\n}\n```\n\n#### HTML 代码 HTML codes\n```html\n<!DOCTYPE html>\n<html>\n    <head>\n        <mate charest=\"utf-8\" />\n        <meta name=\"keywords\" content=\"Editor.md, Markdown, Editor\" />\n        <title>Hello world!</title>\n        <style type=\"text/css\">\n            body{font-size:14px;color:#444;font-family: \"Microsoft Yahei\", Tahoma, \"Hiragino Sans GB\", Arial;background:#fff;}\n            ul{list-style: none;}\n            img{border:none;vertical-align: middle;}\n        </style>\n    </head>\n    <body>\n        <h1 class=\"text-xxl\">Hello world!</h1>\n        <p class=\"text-green\">Plain text</p>\n    </body>\n</html>\n```\n### 图片 Images\n\n图片加链接 (Image + Link)：\n\n\n[![](https://www.mdeditor.com/images/logos/markdown.png)](https://www.mdeditor.com/images/logos/markdown.png \"markdown\")\n\n> Follow your heart.\n\n----\n### 列表 Lists\n\n#### 无序列表（减号）Unordered Lists (-)\n\n- 列表一\n- 列表二\n- 列表三\n\n#### 无序列表（星号）Unordered Lists (*)\n\n* 列表一\n* 列表二\n* 列表三\n\n#### 无序列表（加号和嵌套）Unordered Lists (+)\n+ 列表一\n+ 列表二\n    + 列表二-1\n    + 列表二-2\n    + 列表二-3\n+ 列表三\n    * 列表一\n    * 列表二\n    * 列表三\n\n#### 有序列表 Ordered Lists (-)\n\n1. 第一行\n2. 第二行\n3. 第三行\n\n#### GFM task list\n\n- [x] GFM task list 1\n- [x] GFM task list 2\n- [ ] GFM task list 3\n    - [ ] GFM task list 3-1\n    - [ ] GFM task list 3-2\n    - [ ] GFM task list 3-3\n- [ ] GFM task list 4\n    - [ ] GFM task list 4-1\n    - [ ] GFM task list 4-2\n\n----\n\n### 绘制表格 Tables\n\n| 项目        | 价格   |  数量  |\n| --------   | -----:  | :----:  |\n| 计算机      | $1600   |   5     |\n| 手机        |   $12   |   12   |\n| 管线        |    $1    |  234  |\n\nFirst Header  | Second Header\n------------- | -------------\nContent Cell  | Content Cell\nContent Cell  | Content Cell\n\n| First Header  | Second Header |\n| ------------- | ------------- |\n| Content Cell  | Content Cell  |\n| Content Cell  | Content Cell  |\n\n| Function name | Description                    |\n| ------------- | ------------------------------ |\n| `help()`      | Display the help window.       |\n| `destroy()`   | **Destroy your computer!**     |\n\n| Left-Aligned  | Center Aligned  | Right Aligned |\n| :------------ |:---------------:| -----:|\n| col 3 is      | some wordy text | $1600 |\n| col 2 is      | centered        |   $12 |\n| zebra stripes | are neat        |    $1 |\n\n| Item      | Value |\n| --------- | -----:|\n| Computer  | $1600 |\n| Phone     |   $12 |\n| Pipe      |    $1 |\n\n----\n\n#### 特殊符号 HTML Entities Codes\n\n&copy; &  &uml; &trade; &iexcl; &pound;\n&amp; &lt; &gt; &yen; &euro; &reg; &plusmn; &para; &sect; &brvbar; &macr; &laquo; &middot;\n\nX&sup2; Y&sup3; &frac34; &frac14;  &times;  &divide;   &raquo;\n\n18&ordm;C  &quot;  &apos;\n\n[========]\n\n### Emoji表情 :smiley:\n\n> Blockquotes :star:\n\n#### GFM task lists & Emoji & fontAwesome icon emoji & editormd logo emoji :editormd-logo-5x:\n\n- [x] :smiley: @mentions, :smiley: #refs, [links](), **formatting**, and <del>tags</del> supported :editormd-logo:;\n- [x] list syntax required (any unordered or ordered list supported) :editormd-logo-3x:;\n- [x] [ ] :smiley: this is a complete item :smiley:;\n- [ ] []this is an incomplete item [test link](#) :fa-star: @pandao;\n- [ ] [ ]this is an incomplete item :fa-star: :fa-gear:;\n    - [ ] :smiley: this is an incomplete item [test link](#) :fa-star: :fa-gear:;\n    - [ ] :smiley: this is  :fa-star: :fa-gear: an incomplete item [test link](#);\n\n#### 反斜杠 Escape\n\n\\*literal asterisks\\*\n\n[========]\n### 科学公式 TeX(KaTeX)\n\n$$E=mc^2$$\n\n行内的公式$$E=mc^2$$行内的公式，行内的$$E=mc^2$$公式。\n\n$$x > y$$\n\n$$\\(\\sqrt{3x-1}+(1+x)^2\\)$$\n\n$$\\sin(\\alpha)^{\\theta}=\\sum_{i=0}^{n}(x^i + \\cos(f))$$\n\n多行公式：\n\n```math\n\\displaystyle\n\\left( \\sum\\_{k=1}^n a\\_k b\\_k \\right)^2\n\\leq\n\\left( \\sum\\_{k=1}^n a\\_k^2 \\right)\n\\left( \\sum\\_{k=1}^n b\\_k^2 \\right)\n```\n```katex\n\\displaystyle\n    \\frac{1}{\n        \\Bigl(\\sqrt{\\phi \\sqrt{5}}-\\phi\\Bigr) e^{\n        \\frac25 \\pi}} = 1+\\frac{e^{-2\\pi}} {1+\\frac{e^{-4\\pi}} {\n        1+\\frac{e^{-6\\pi}}\n        {1+\\frac{e^{-8\\pi}}\n         {1+\\cdots} }\n        }\n    }\n```\n```latex\nf(x) = \\int_{-\\infty}^\\infty\n    \\hat f(\\xi)\\,e^{2 \\pi i \\xi x}\n    \\,d\\xi\n```\n### 分页符 Page break\n\n> Print Test: Ctrl + P\n\n[========]\n\n### 绘制流程图 Flowchart\n\n```flow\nst=>start: 用户登陆\nop=>operation: 登陆操作\ncond=>condition: 登陆成功 Yes or No?\ne=>end: 进入后台\n\nst->op->cond\ncond(yes)->e\ncond(no)->op\n```\n[========]\n\n### 绘制序列图 Sequence Diagram\n\n```seq\nAndrew->China: Says Hello\nNote right of China: China thinks\\nabout it\nChina-->Andrew: How are you?\nAndrew->>China: I am good thanks!\n```\n### End', '0');

-- ----------------------------
-- Table structure for document_group
-- ----------------------------
DROP TABLE IF EXISTS `document_group`;
CREATE TABLE `document_group` (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `document_id` bigint(11) NOT NULL COMMENT '文档id',
  `parent_id` bigint(11) NOT NULL COMMENT '父类id，0表示一级',
  `category_id` bigint(11) NOT NULL COMMENT '分类id',
  `order_id` int(2) NOT NULL DEFAULT '0' COMMENT '展示的顺序，从0开始',
  `title` varchar(100) NOT NULL COMMENT '标题',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE KEY `idx_document_id` (`document_id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_category_id` (`category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='树形文档';

-- ----------------------------
-- Records of document_group
-- ----------------------------
INSERT INTO `document_group` VALUES ('1', '11856480811618304', '0', '11676648115015680', '0', '第一章 初始Java');
INSERT INTO `document_group` VALUES ('2', '11861475942469632', '0', '11676648115015680', '1', '第二章 基本数据类型');
INSERT INTO `document_group` VALUES ('3', '12146786274643968', '0', '11676648115015680', '2', '第三章 条件结构、循环语句');
INSERT INTO `document_group` VALUES ('4', '13467517826633728', '12146786274643968', '11676648115015680', '0', '条件结构');
INSERT INTO `document_group` VALUES ('5', '13470909777055744', '12146786274643968', '11676648115015680', '1', '循环结构');
INSERT INTO `document_group` VALUES ('6', '13521233892085760', '13467517826633728', '11676648115015680', '0', '三级目录测试');
INSERT INTO `document_group` VALUES ('7', '13526443972628480', '11856480811618304', '11676648115015680', '0', 'markdown语法测试');

-- ----------------------------
-- Table structure for local_o_auth
-- ----------------------------
DROP TABLE IF EXISTS `local_o_auth`;
CREATE TABLE `local_o_auth` (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `salt` varchar(64) NOT NULL COMMENT '盐值',
  `password` varchar(64) NOT NULL COMMENT '密码',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='管理员认证';

-- ----------------------------
-- Records of local_o_auth
-- ----------------------------
INSERT INTO `local_o_auth` VALUES ('1', '11676640061952000', '40e11265427f75e0f1ac9f3bbb6a41ac3b938867661fa04001c4c917a46937ad', 'c7bcdab6e9558eb93ee6b2963ae989553f2196df1587aefa2b1dece465fcf55d');

-- ----------------------------
-- Table structure for local_user
-- ----------------------------
DROP TABLE IF EXISTS `local_user`;
CREATE TABLE `local_user` (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `username` varchar(45) CHARACTER SET utf8 NOT NULL COMMENT '昵称',
  `email` varchar(45) NOT NULL COMMENT '邮箱(登录名)',
  `sex` char(2) DEFAULT 'M' COMMENT '用户性别',
  `avatar` varchar(255) NOT NULL DEFAULT 'aHR0cHM6Ly9hdmF0YXJzMS5naXRodWJ1c2VyY29udGVudC5jb20vdS8yMzQwMTY5MT9zPTQ2MCZ2PTQ=' COMMENT '头像',
  `description` varchar(150) DEFAULT '简单一句话，介绍你自己' COMMENT '简介',
  `github` varchar(100) DEFAULT NULL COMMENT 'github',
  `twitter` varchar(100) DEFAULT NULL COMMENT 'twitter',
  `weibo` varchar(100) DEFAULT NULL COMMENT 'weibo',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE KEY `uk_email` (`email`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户基本信息表';

-- ----------------------------
-- Records of local_user
-- ----------------------------
INSERT INTO `local_user` VALUES ('1', '11676640061952000', 'AkaneMurakwa', 'chenshjing@gmail.com', null, 'aHR0cHM6Ly9hdmF0YXJzMS5naXRodWJ1c2VyY29udGVudC5jb20vdS8yMzQwMTY5MT9zPTQ2MCZ2PTQ=', null, null, null, null);

-- ----------------------------
-- Table structure for third_o_auth
-- ----------------------------
DROP TABLE IF EXISTS `third_o_auth`;
CREATE TABLE `third_o_auth` (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `username` varchar(45) NOT NULL COMMENT '昵称',
  `email` varchar(45) NOT NULL COMMENT '邮箱(登录名)',
  `avatar` varchar(255) NOT NULL DEFAULT 'aHR0cHM6Ly9hdmF0YXJzMS5naXRodWJ1c2VyY29udGVudC5jb20vdS8yMzQwMTY5MT9zPTQ2MCZ2PTQ=' COMMENT '头像',
  `token` varchar(255) NOT NULL,
  `nickname` varchar(100) DEFAULT NULL COMMENT '别名',
  `blog` varchar(100) DEFAULT NULL COMMENT '博客',
  `company` varchar(100) DEFAULT NULL COMMENT '公司',
  `location` varchar(100) DEFAULT NULL COMMENT '位置',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE KEY `uk_email` (`email`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE,
  UNIQUE KEY `uk_token` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='第三方用户基本信息表';

-- ----------------------------
-- Records of third_o_auth
-- ----------------------------
INSERT INTO `third_o_auth` VALUES ('1', '23401691', 'AkaneMurakawa', 'chenshjing@gmail.com', 'aHR0cHM6Ly9hdmF0YXJzMC5naXRodWJ1c2VyY29udGVudC5jb20vdS8yMzQwMTY5MT92PTQ=', '509d288f05999d8e3b9507fb024f9be3ba5fef4f', 'Akane Murakawa', 'http://yuumasan.com/', null, null, '如果你都不知道自己想去哪里，那去哪里都是一样的。');
