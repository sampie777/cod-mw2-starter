package nl.sajansen.codmw2starter.cod

enum class MapName(val codeName: String) {
    Afghan("mp_afghan"),
    Derail("mp_derail"),
    Estate("mp_estate"),
    Favela("mp_favela"),
    Highrise("mp_highrise"),
    Invasion("mp_invasion"),
    Karachi("mp_checkpoint"),
    Quarry("mp_quarry"),
    Rundown("mp_rundown"),
    Rust("mp_rust"),
    Scrapyard("mp_boneyard"),
    Skidrow("mp_nightshift"),
    Subbase("mp_subbase"),
    Terminal("mp_terminal"),
    Underpass("mp_underpass"),
    Wasteland("mp_brecourt"),
}

enum class GameType(val codeName: String) {
    Free_For_All("dm"),
    Team_Death_Match("war"),
    Domination("dom"),
    Sabotage("sab"),
    One_Flag("oneflag"),
    Headquarters("koth"),
    Global_Thermo_Nuclear_War("gtnw"),
    Search_and_Destroy("sd"),
    Capture_the_flag("ctf"),
    VIP("vip"),
}

enum class Spectate(val codeName: String) {
    None("none"),
    Team("team"),
    Free("free"),
}