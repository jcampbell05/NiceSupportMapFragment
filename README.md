NiceSupportMapFragment
======================

If you've used the SupportMapFragment on older Android devices then you know how annoying it can be when it leaves a black box whenever you scroll a ListView, ScrollView or a ViewPager.

The reason behind this is that the Map uses a SurfaceView, when the App creates this view it creates a Window behind your Activity's and punches a hole meaning things in your Activity's window cannot seen underneath the SurfaceView's original location. 

Whilst there may be a plethora of fixes for this, none of them are quick or easy. Whilst it would be amazing if google could fix it, for now the quick and easy way is to use this small replacement class that handles making sure that no black boxes are left behind. Although still very janky, it works and is reccomended for anyone who uses Google Maps SDK v2 on older devices.

In addition to that we also implemented some additional tweaks to help reduce the black flicker bug that occurs when a MapView is created (due to it using the SurfaceView), in some cases we've managed to make it not flicker at all. 

And finally we have a way of disabling that annoying quirk where a MapView in a containing view like a ScrollView would have it's gestures swallowed so a user couldn't pan the MapView, we automatically stop this from happening but you can opt-out of this behaviour (see below). We fully support TextureView on newer devices as well for this behavior.

We love all the amazing work you Android Developers give to the Open Source Community so this is our small way of giving something back.

Recommended for Android 2.3 and Above

Original Fix which this Class is based upon -  https://github.com/jfeinstein10/SlidingMenu/issues/168

Special thanks to to Gemerson Ribas (gmribas) and David Thompson (Pepsi1x1) for there help.

Install
-------

1. Add NiceSupportMapFragment.jar to your Project's Lib Directory.
2. Replace your Fragment code in your Layout to use this class.

        <fragment
        android:id="@+id/map"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:name="com.NYXDigital.NiceSupportMapFragment"/>

3. Replace all references to the Fragment in your code to use this class.

        NiceSupportMapFragment mapFragment = (NiceSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        GoogleMap mMap = mapFragment.getMap();

4. Build and Run - It should just work, *phew* that was easy!

We also have support for allowing user's to vertical and horizontal pan a map when it is placed inside of a view that also uses these gestures i.e Scroll Views, where normally if you tried to pan the map the scroll view would move. This behaviour is enabled by default but to turn it off just set preventParentScrolling to false.
       
       mapFragment.setPreventParentScrolling(false);

License
-------

    Copyright 2013 Nyx Digital

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
