import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

// Import types
import type { RootStackParamList } from './types';

// Import screens
import {
  HomeScreen,
  NotificationScreen,
  DeviceInfoScreen,
  ContactKeyScreen,
  PlaceholderScreen,
  InAppMessageScreen,
  InAppInlineScreen,
} from './screens';

const Stack = createStackNavigator<RootStackParamList>();

const App = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="Notification" component={NotificationScreen} />
        <Stack.Screen name="DeviceInfo" component={DeviceInfoScreen} />
        <Stack.Screen name="ContactKey" component={ContactKeyScreen} />
        {/* <Stack.Screen name="InboxMessages" component={PlaceholderScreen} /> */}
        {/* <Stack.Screen name="CustomEvent" component={PlaceholderScreen} /> */}
        <Stack.Screen name="InAppMessages" component={InAppMessageScreen} />
        {/* <Stack.Screen name="RealTimeInApp" component={PlaceholderScreen} /> */}
        {/* <Stack.Screen name="SetTags" component={PlaceholderScreen} /> */}
        {/* <Stack.Screen name="Geofence" component={PlaceholderScreen} /> */}
        <Stack.Screen name="InlineInApp" component={InAppInlineScreen} />
        <Stack.Screen name="AppStory" component={PlaceholderScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;