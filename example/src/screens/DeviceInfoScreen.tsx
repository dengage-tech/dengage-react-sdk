import { useEffect, useState } from 'react';
import {
  ScrollView,
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ToastAndroid,
  Alert,
  Dimensions,
  Platform,
  NativeModules,
} from 'react-native';
import Clipboard from '@react-native-clipboard/clipboard';
import Dengage from '@dengage-tech/react-native-dengage';

const { PlatformConstants } = NativeModules;

const DeviceInfoScreen = () => {

  // State for async values
  const [deviceId, setDeviceId] = useState<string>('');
  const [contactKey, setContactKey] = useState<string>('');
  const [token, setToken] = useState<string>('');
  const [sdkVersion, setSdkVersion] = useState<string>('');
  const [permission, setPermission] = useState<string>('');

  // Trigger page view analytics
  useEffect(() => {
    //if (Dengage.sendPageView) {
    //  Dengage.sendPageView('device-info');
    //}

    // Fetch promise-based values
    Dengage.getSubscription?.().then(sub => {
      if (sub?.contactKey) {
        setContactKey(sub.contactKey);
      }
      if (sub?.deviceId) {
        setDeviceId(sub.deviceId);
      }
      if (sub?.token) {
        setToken(sub.token);
      }
      if (sub?.sdkVersion) {
        setSdkVersion(sub.sdkVersion);
      }
      if (sub?.permission) {
        setPermission(sub.permission ? 'true' : 'false');
      }
      if (sub?.sdkVersion) {
        setSdkVersion(sub.sdkVersion);
      }
    }).catch((err) => {
      console.error('Error fetching subscription:', err);
    });

    //Dengage.getDeviceId?.().then(val => setDeviceId(val ?? '')).catch(() => {});
    //Dengage.getContactKey?.().then(val => setContactKey(val ?? '')).catch(() => {});
    //Dengage.getToken?.().then(val => setToken(val ?? '')).catch(() => {});
    //Dengage.getSdkVersion?.().then(val => setSdkVersion(val ?? '')).catch(() => {});
  }, []);


  const brand = PlatformConstants?.Manufacturer || Platform.OS;
  const model = PlatformConstants?.Model || 'Unknown';
  const { width, height } = Dimensions.get('window');
  const osVersion = Platform.Version;

  const copyToClipboard = (label: string, value: string) => {
    if (!value) return;
    Clipboard.setString(value);
    if (Platform.OS === 'android') {
      ToastAndroid.show(`${label} copied`, ToastAndroid.SHORT);
    } else {
      Alert.alert('Copied', `${label} copied to clipboard`);
    }
  };

  const renderRow = (
    label: string,
    value: string,
    copyable: boolean = false
  ) => (
    <View style={styles.row} key={label}>
      <Text style={styles.label}>{label}:</Text>
      {copyable ? (
        <TouchableOpacity onPress={() => copyToClipboard(label, value)}>
          <Text style={[styles.value, styles.copyable]}>{value}</Text>
        </TouchableOpacity>
      ) : (
        <Text style={styles.value}>{value}</Text>
      )}
    </View>
  );

  return (
    <ScrollView contentContainerStyle={styles.container}>
      {renderRow('Integration Key', "")}
      {renderRow('Device ID', deviceId, true)}
      {renderRow('Contact Key', contactKey, true)}
      {renderRow('User Permission', permission)}
      {renderRow('Device Token', token, true)}
      {renderRow('Device Brand', brand, true)}
      {renderRow('Device Model', model, true)}
      {renderRow('Advertising ID', "advertisingId")}
      {renderRow('Time Zone', "timeZone")}
      {renderRow('Language', "language")}
      {renderRow('SDK Version', sdkVersion)}
      {renderRow('Screen Width', width.toString())}
      {renderRow('Screen Height', height.toString())}
      {renderRow('OS Version', osVersion.toString())}
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 16,
    backgroundColor: '#fff',
  },
  row: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginBottom: 12,
  },
  label: {
    fontWeight: 'bold',
    marginRight: 6,
    fontSize: 16,
    color: '#000',
  },
  value: {
    fontSize: 16,
    color: '#000',
  },
  copyable: {
    textDecorationLine: 'underline',
  },
});

export default DeviceInfoScreen;