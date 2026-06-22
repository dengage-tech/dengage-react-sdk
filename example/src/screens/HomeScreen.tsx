import React from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet } from 'react-native';
import type { StackNavigationProp } from '@react-navigation/stack';
import Dengage from '@dengage-tech/react-native-dengage';
import type { RootStackParamList } from '../types';

type HomeScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Home'>;

type HomeScreenProps = {
  navigation: HomeScreenNavigationProp;
};

const HomeScreen: React.FC<HomeScreenProps> = ({ navigation }) => {
  
  const actions: Array<{ title: string; screen: keyof RootStackParamList }> = [
    { title: 'ASK NOTIFICATIONS', screen: 'Notification' },
    { title: 'DEVICE INFO', screen: 'DeviceInfo' },
    { title: 'SHOW SUBSCRIPTION', screen: 'Subscription' },
    { title: 'CHANGE CONTACT KEY', screen: 'ContactKey' },
    { title: 'CHANGE TRACKING PERMISSION', screen: 'TrackingPermission' },
    { title: 'INBOX MESSAGES', screen: 'InboxMessages' },
    //{ title: 'SEND CUSTOM EVENT', screen: 'CustomEvent' },
    { title: 'IN APP MESSAGE', screen: 'InAppMessages' },
    { title: 'REAL TIME IN APP MESSAGE', screen: 'RTInAppMessages' },
    { title: 'REAL TIME IN APP FILTERS', screen: 'RealTimeInAppFilters' },
    //{ title: 'REAL TIME IN APP MESSAGES', screen: 'RealTimeInApp' },
    //{ title: 'SET TAGS', screen: 'SetTags' },
    { title: 'GEOFENCE', screen: 'Geofence' },
    { title: 'INAPP INLINE ', screen: 'InlineInApp' },
    { title: 'APP STORY', screen: 'AppStory' },
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
              if (item.screen === 'Notification') {
                console.log(item.screen);
                console.log('promptForPushNotifications');
                Dengage.promptForPushNotifications();
              } else {
                navigation.navigate(item.screen);
              }
            }}>
            <Text style={styles.buttonText}>{item.title}</Text>
          </TouchableOpacity>
        )}
      />
    </View>
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
});

export default HomeScreen;