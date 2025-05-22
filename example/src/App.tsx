import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

// Import types
import type { RootStackParamList } from './types';

console.log('App.tsx: App loaded');

// Import screens
import {
  HomeScreen,
  NotificationScreen,
  DeviceInfoScreen,
  ContactKeyScreen,
  InAppMessageScreen,
  InAppInlineScreen,
  AppStoryScreen,
  GeofenceScreen,
  InboxMessagesScreen,
  RTInAppMessagesScreen,
} from './screens';
import React from 'react';
import { NativeEventEmitter, NativeModules } from 'react-native';

const Stack = createStackNavigator<RootStackParamList>();

const App = () => {
  let eventListener: import('react-native').EmitterSubscription | undefined;

  React.useEffect(() => {
    const eventEmitter = new NativeEventEmitter(NativeModules.DengageRN);
    eventListener = eventEmitter.addListener(
      'onNotificationClicked',
      (event) => {
        console.log('--------------------');
        console.log('onNotificationClicked');
        console.log(event);
        console.log('--------------------');
        console.log('onNotificationClicked');
      }
    );

    return () => {
      eventListener?.remove?.();
    };
  }, []);

  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="Notification" component={NotificationScreen} />
        <Stack.Screen name="DeviceInfo" component={DeviceInfoScreen} />
        <Stack.Screen name="ContactKey" component={ContactKeyScreen} />
        <Stack.Screen name="InboxMessages" component={InboxMessagesScreen} />
        {/* <Stack.Screen name="CustomEvent" component={PlaceholderScreen} /> */}
        <Stack.Screen name="InAppMessages" component={InAppMessageScreen} />
        <Stack.Screen name="RTInAppMessages" component={RTInAppMessagesScreen} />
        {/* <Stack.Screen name="RealTimeInApp" component={PlaceholderScreen} /> */}
        {/* <Stack.Screen name="SetTags" component={PlaceholderScreen} /> */}
        <Stack.Screen name="Geofence" component={GeofenceScreen} />
        <Stack.Screen name="InlineInApp" component={InAppInlineScreen} />
        <Stack.Screen name="AppStory" component={AppStoryScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;
