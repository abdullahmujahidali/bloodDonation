# bloodDonation
A small mobile application of blood donation developed using JAVA

Requirement: You need to make an application for blood donation. In which there must be option to
add Blood Donors. Donor information can be his name, phone number, address. There must be three
Fragments on MainActivity
1. ListFragment which will have list of donors. Every item in the list must have name and blood
group of donor.
2. DetailFragment will show the information of donor on which you have clicked. There must
be one TextView for name, two ImageViews for call and address respectively. Then user will
click on “call ImageView”, it must open dialer with respective mobile number of donor. And
if user will click on “location ImageView”, google map should be open.
3. AddNewDonorFragment will show two buttons i.e. “Add New Donor” Button and “Search
Donor By Name” Button.
4. When user will click on Add New Donor Button, there must be new activity opened on
which you will take all information of a donor and press submit button. It will add donor in
the list of already present donors. Your activity will be finished after adding record and then
again MainActivity will showed up with updated records.
5. When user will click on Search Donor By Name Button, there must be new activity open and
it will ask the name of the donor which you want to search. If provided name of donor is
present in the list of exisiting donors then show its information by using textView. Otherwise
show appropriate message to the user.
