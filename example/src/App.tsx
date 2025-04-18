import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
  Switch,
  TextInput,
  Button,
} from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator, type StackNavigationProp } from '@react-navigation/stack';
import { type RouteProp } from '@react-navigation/native';

import { promptForPushNotifications } from '@dengage-tech/react-native-dengage';
//import { multiply } from '@dengage-tech/react-native-dengage';


type RootStackParamList = {
  Home: undefined;
  Notification: undefined;
  DeviceInfo: undefined;
  ContactKey: undefined;
  InboxMessages: undefined;
  CustomEvent: undefined;
  InAppMessages: undefined;
  RealTimeInApp: undefined;
  SetTags: undefined;
  TestPage: undefined;
  Geofence: undefined;
  InlineInApp: undefined;
  AppStory: undefined;
};

type HomeScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Home'>;
type HomeScreenRouteProp = RouteProp<RootStackParamList, 'Home'>;

type HomeScreenProps = {
  navigation: HomeScreenNavigationProp;
  route: HomeScreenRouteProp;
};

type PlaceholderScreenProps = {
  route: RouteProp<RootStackParamList, keyof RootStackParamList>;
};

const HomeScreen: React.FC<HomeScreenProps> = ({ navigation }) => {
  const actions: Array<{ title: string; screen: keyof RootStackParamList }> = [
    { title: 'ASK NOTIFICATIONS', screen: 'Notification' },
    { title: 'DEVICE INFO', screen: 'DeviceInfo' },
    { title: 'CHANGE CONTACT KEY', screen: 'ContactKey' },
    { title: 'INBOX MESSAGES', screen: 'InboxMessages' },
    { title: 'SEND CUSTOM EVENT', screen: 'CustomEvent' },
    { title: 'IN APP MESSAGES', screen: 'InAppMessages' },
    { title: 'REAL TIME IN APP MESSAGES', screen: 'RealTimeInApp' },
    { title: 'SET TAGS', screen: 'SetTags' },
    { title: 'DENGAGE TEST PAGE', screen: 'TestPage' },
    { title: 'GEOFENCE', screen: 'Geofence' },
    { title: 'Show InLine InAPP', screen: 'InlineInApp' },
    { title: 'App Story', screen: 'AppStory' },
  ];
  

  return (
    <View style={styles.container}>
      <FlatList
        data={actions}
        keyExtractor={(item) => item.title}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.button}
            onPress={() => {

              if (item.screen == 'Notification') {
                console.log('promptForPushNotifications');
                promptForPushNotifications();
              } else {
                navigation.navigate(item.screen)

              }
            }}>
            <Text style={styles.buttonText}>{item.title}</Text>
          </TouchableOpacity>
        )}
      />
    </View>
  );
};





const Stack = createStackNavigator<RootStackParamList>();



const NotificationScreen = () => (
  <View style={styles.screenContainer}>
    <Text>ASK NOTIFICATIONS Screen</Text>
  </View>
);

const DeviceInfoScreen = () => {
  const deviceInfo = {
    deviceId: 'e1134bc1-ca9a-4a05-a05f-186ed9eef505',
    contactKey: 'egemen',
    userPermission: true,
    deviceToken: '8001816aa2ffec208e91a2cc44a5d239320095f731a32e2848ddfe',
    bundleIdentifier: 'com.dengage.Example',
    sdkVersion: '5.73',
  };

  return (
    <View style={styles.screenContainer}>
      {Object.entries(deviceInfo).map(([key, value]) => (
        <Text key={key}>
          {key}: {value.toString()}
        </Text>
      ))}
    </View>
  );
};

const ContactKeyScreen = () => {
  const [contactKey, setContactKey] = React.useState('egemen');
  const [permission, setPermission] = React.useState(true);

  return (
    <View style={styles.screenContainer}>
      <TextInput
        style={styles.input}
        placeholder="Enter Contact Key"
        value={contactKey}
        onChangeText={setContactKey}
      />
      <Button title="Set Contact Key" onPress={() => console.log('Contact Key:', contactKey)} />
      <View style={styles.switchContainer}>
        <Text>Permission</Text>
        <Switch
          value={permission}
          onValueChange={(value) => setPermission(value)}
        />
      </View>
    </View>
  );
};

const PlaceholderScreen: React.FC<PlaceholderScreenProps> = ({ route }) => (
  <View style={styles.screenContainer}>
    <Text>{route.name} Screen</Text>
  </View>
);


const App = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="Notification" component={NotificationScreen} />
        <Stack.Screen name="DeviceInfo" component={DeviceInfoScreen} />
        <Stack.Screen name="ContactKey" component={ContactKeyScreen} />
        <Stack.Screen name="InboxMessages" component={PlaceholderScreen} />
        <Stack.Screen name="CustomEvent" component={PlaceholderScreen} />
        <Stack.Screen name="InAppMessages" component={PlaceholderScreen} />
        <Stack.Screen name="RealTimeInApp" component={PlaceholderScreen} />
        <Stack.Screen name="SetTags" component={PlaceholderScreen} />
        <Stack.Screen name="TestPage" component={PlaceholderScreen} />
        <Stack.Screen name="Geofence" component={PlaceholderScreen} />
        <Stack.Screen name="InlineInApp" component={PlaceholderScreen} />
        <Stack.Screen name="AppStory" component={PlaceholderScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#ffffff',
  },
  button: {
    padding: 16,
    marginVertical: 8,
    backgroundColor: '#d3d3d3',
    alignItems: 'center',
    borderRadius: 8,
  },
  buttonText: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  screenContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  input: {
    borderWidth: 1,
    borderColor: '#cccccc',
    borderRadius: 8,
    padding: 8,
    width: '80%',
    marginBottom: 16,
    textAlign: 'center',
  },
  switchContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 16,
  },
});

export default App;