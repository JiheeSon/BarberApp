package com.example.barberapp.model.remote.response.service

data class Services(
    val beardStyles: List<BeardStyle>,
    val comboOffers: List<ComboOffer>,
    val hairColors: List<HairColor>,
    val haircuts: List<Haircut>,
    val headMassage: List<HeadMassage>,
    val massagesSpa: List<MassagesSpa>,
    val officialLooks: List<OfficialLook>,
    val shaves: List<Shave>,
    val facial: List<Facial>
)