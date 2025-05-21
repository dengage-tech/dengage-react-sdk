import { useEffect, useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Alert,
  ToastAndroid,
  Platform,
  Dimensions,
  PixelRatio,
  NativeModules,
  ScrollView,
} from 'react-native';
import DeviceInfo from 'react-native-device-info';
import Clipboard from '@react-native-clipboard/clipboard';
import Dengage from '@dengage-tech/react-native-dengage';

const { PlatformConstants } = NativeModules;

export default function DeviceInfoScreen() {
  const [info, setInfo] = useState({
    integrationKey: '',
    deviceId: '',
    contactKey: '',
    token: '',
    sdkVersion: '',
    permission: '',
  });

  useEffect(() => {
    Dengage.getSubscription?.()
      .then((sub) =>
        setInfo({
          integrationKey: sub?.integrationKey || '',
          deviceId: sub?.deviceId || '',
          contactKey: sub?.contactKey || '',
          token: sub?.token || '',
          sdkVersion: sub?.sdkVersion || '',
          permission: sub?.permission ? 'true' : 'false',
        })
      )
      .catch((err) => console.error('Error fetching subscription:', err));
  }, []);

  const brand = PlatformConstants?.Manufacturer || Platform.OS;
  const model = PlatformConstants?.Model || 'Unknown';
  const { width, height } = Dimensions.get('window');
  const pixelRatio = PixelRatio.get();
  const realWidth = Math.round(width * pixelRatio);
  const realHeight = Math.round(height * pixelRatio);
  const osVersion = DeviceInfo.getSystemVersion();

  const fields = [
    { label: 'Integration Key', value: info.integrationKey },
    { label: 'Device ID', value: info.deviceId },
    { label: 'Contact Key', value: info.contactKey },
    { label: 'User Permission', value: info.permission },
    { label: 'Device Token', value: info.token },
    { label: 'Device Brand', value: brand },
    { label: 'Device Model', value: model },
    { label: 'SDK Version', value: info.sdkVersion },
    { label: 'Screen Width', value: realWidth.toString() },
    { label: 'Screen Height', value: realHeight.toString() },
    { label: 'OS Version', value: osVersion.toString() },
  ];

  const handleCopy = (label: string, value: string) => {
    if (!value) return;
    Clipboard.setString(value);
    Platform.OS === 'android'
      ? ToastAndroid.show(`${label} copied`, ToastAndroid.SHORT)
      : Alert.alert('Copied', `${label} copied to clipboard`);
  };

  return (
    <ScrollView contentContainerStyle={styles.list}>
      <View style={styles.card}>
        {fields.map((item, idx) => (
          <View key={item.label}>
            <TouchableOpacity
              style={styles.row}
              onLongPress={() => handleCopy(item.label, item.value)}
              activeOpacity={0.7}
            >
              <Text style={styles.cellLabel}>{item.label}</Text>
              <Text style={styles.cellValue}>{item.value}</Text>
            </TouchableOpacity>
            {idx !== fields.length - 1 && <View style={styles.separator} />}
          </View>
        ))}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  list: {
    padding: 16,
    backgroundColor: '#f2f2f7',
    flexGrow: 1,
  },
  card: {
    backgroundColor: '#fff',
    borderRadius: 16,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOpacity: 0.04,
    shadowRadius: 2,
    shadowOffset: { width: 0, height: 1 },
    elevation: 1,
  },
  row: {
    paddingVertical: 12,
    paddingHorizontal: 16,
  },
  cellLabel: {
    fontSize: 12,
    color: '#888',
    marginBottom: 2,
  },
  cellValue: {
    fontSize: 13,
    color: '#222',
    fontWeight: '500',
  },
  separator: {
    height: 1,
    backgroundColor: '#ececec',
    marginLeft: 16,
    marginRight: 16,
  },
});
