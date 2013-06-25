NiceSupportMapFragment
======================

If you've used the SupportMapFragment on older Android devices then you know how annoying it can be when it leaves a black box whenever you scroll a ListView, ScrollView or a ViewPager.

The reason behind this is that the Map uses a SurfaceView, when the App creates this view it creates a Window behind your activity's and punches a hole meaning things in your activity's window cannot seen underneath the SurfaceView's original location. 

Whilst there may be a plethora of fixes for this and in the long run it would be amazing if google could fix it, but for now the quick and easy way is to use this small replacement class that handles making sure that no black boxes are left behind. Although still very janky, it works and is reccomended for anyone who uses Google Maps SDK v2 on older devices.

We love all the amazing work you Android Devlopers give to the Open Source Community so this is our small way of giving somthing back.

Original Fix which this Class is based upon -  https://github.com/jfeinstein10/SlidingMenu/issues/168

Install
-------

1. Add NiceSupportMapFragment.jar to Project's Lib Directory.
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

We also add support for allowing user's to vertical and horizontal pan a map when it is placed inside of a view that also uses these gestures i.e Scroll Views. This behaviour is enabled by default but to turn it off just set the preventParentScrolling variable to false.
   mapFragment.setPreventParentScrolling(false);


License
-------

    Copyright 2013 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
