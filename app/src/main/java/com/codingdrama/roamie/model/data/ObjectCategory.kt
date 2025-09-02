package com.codingdrama.roamie.model.data

/**
 * Ref:
 * https://cocodataset.org/#explore
 * https://docs.aws.amazon.com/rekognition/latest/customlabels-dg/md-transform-coco.html
 *
 * Based on COCO dataset categories at first 80 items
 * It will become the standard for object detection and recognition in the app.
 * It should match a model trained on COCO dataset.
 * However, if you have a custom model, DO NOT change id, label, emoji order.
 * You should make your model label, id to match this enum class.
 *
 * If you want to add more categories, please add at the end of the list.
 * DO NOT change id, label, emoji order at first 80 items.
 */

enum class ObjectCategory(val id: Int, val label: String, val emoji: String) {
    PERSON(1, "person", "🧍"),
    BICYCLE(2, "bicycle", "🚲"),
    CAR(3, "car", "🚗"),
    MOTORCYCLE(4, "motorcycle", "🏍️"),
    AIRPLANE(5, "airplane", "✈️"),
    BUS(6, "bus", "🚌"),
    TRAIN(7, "train", "🚆"),
    TRUCK(8, "truck", "🚚"),
    BOAT(9, "boat", "⛴️"),
    TRAFFIC_LIGHT(10, "traffic light", "🚦"),
    FIRE_HYDRANT(11, "fire hydrant", "🧯"),
    STOP_SIGN(12, "stop sign", "🛑"),
    PARKING_METER(13, "parking meter", "🅿️"),
    BENCH(14, "bench", "🪑"),
    BIRD(15, "bird", "🐦"),
    CAT(16, "cat", "🐱"),
    DOG(17, "dog", "🐶"),
    HORSE(18, "horse", "🐴"),
    SHEEP(19, "sheep", "🐑"),
    COW(20, "cow", "🐄"),
    ELEPHANT(21, "elephant", "🐘"),
    BEAR(22, "bear", "🐻"),
    ZEBRA(23, "zebra", "🦓"),
    GIRAFFE(24, "giraffe", "🦒"),
    BACKPACK(25, "backpack", "🎒"),
    UMBRELLA(26, "umbrella", "☂️"),
    HANDBAG(27, "handbag", "👜"),
    TIE(28, "tie", "👔"),
    SUITCASE(29, "suitcase", "🧳"),
    FRISBEE(30, "frisbee", "🥏"),
    SKIS(31, "skis", "🎿"),
    SNOWBOARD(32, "snowboard", "🏂"),
    SPORTS_BALL(33, "sports ball", "⚽"),
    KITE(34, "kite", "🪁"),
    BASEBALL_BAT(35, "baseball bat", "🏏"),
    BASEBALL_GLOVE(36, "baseball glove", "🥎"),
    SKATEBOARD(37, "skateboard", "🛹"),
    SURFBOARD(38, "surfboard", "🏄"),
    TENNIS_RACKET(39, "tennis racket", "🎾"),
    BOTTLE(40, "bottle", "🍾"),
    WINE_GLASS(41, "wine glass", "🍷"),
    CUP(42, "cup", "🥤"),
    FORK(43, "fork", "🍴"),
    KNIFE(44, "knife", "🔪"),
    SPOON(45, "spoon", "🥄"),
    BOWL(46, "bowl", "🥣"),
    BANANA(47, "banana", "🍌"),
    APPLE(48, "apple", "🍎"),
    SANDWICH(49, "sandwich", "🥪"),
    ORANGE(50, "orange", "🍊"),
    BROCCOLI(51, "broccoli", "🥦"),
    CARROT(52, "carrot", "🥕"),
    HOT_DOG(53, "hot dog", "🌭"),
    PIZZA(54, "pizza", "🍕"),
    DONUT(55, "donut", "🍩"),
    CAKE(56, "cake", "🍰"),
    CHAIR(57, "chair", "🪑"),
    COUCH(58, "couch", "🛋️"),
    POTTED_PLANT(59, "potted plant", "🪴"),
    BED(60, "bed", "🛏️"),
    DINING_TABLE(61, "dining table", "🍽️"),
    TOILET(62, "toilet", "🚽"),
    TV(63, "tv", "📺"),
    LAPTOP(64, "laptop", "💻"),
    MOUSE(65, "mouse", "🖱️"),
    REMOTE(66, "remote", "🎛️"),
    KEYBOARD(67, "keyboard", "⌨️"),
    CELL_PHONE(68, "cell phone", "📱"),
    MICROWAVE(69, "microwave", "📡"), // 沒有 microwave emoji, 暫用
    OVEN(70, "oven", "🍞"), // 暫用
    TOASTER(71, "toaster", "🍞"), // 暫用
    SINK(72, "sink", "🚰"),
    REFRIGERATOR(73, "refrigerator", "🧊"),
    BOOK(74, "book", "📖"),
    CLOCK(75, "clock", "⏰"),
    VASE(76, "vase", "🏺"),
    SCISSORS(77, "scissors", "✂️"),
    TEDDY_BEAR(78, "teddy bear", "🧸"),
    HAIR_DRIER(79, "hair drier", "💈"), // 沒有吹風機 emoji, 暫用
    TOOTHBRUSH(80, "toothbrush", "🪥");

    companion object {
        fun fromId(id: Int): ObjectCategory? = ObjectCategory.entries.find { it.id == id }
    }
}
