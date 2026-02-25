import { useState, useEffect } from 'react';
import {
  View,
  Text,
  Button,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  KeyboardAvoidingView,
  Platform,
} from 'react-native';
import NoCapTextInput from '../components/NoCapTextInput';
import DeviceInfoItemCell from '../components/DeviceInfoItemCell';
import Dengage from '@dengage-tech/react-native-dengage';

export default function InAppMessageScreen() {
  const [screenName, setScreenName] = useState('');
  const [deviceInfoList, setDeviceInfoList] = useState([
    { key: '', value: '' },
  ]);

  useEffect(() => {
    Dengage.getInAppDeviceInfo().then((info) => {
      const arr = Object.entries(info || {}).map(([key, value]) => ({
        key,
        value,
      }));
      setDeviceInfoList(
        arr.length ? [...arr, { key: '', value: '' }] : [{ key: '', value: '' }]
      );
    });
  }, []);

  const updateDeviceInfo = (
    idx: number,
    field: 'key' | 'value',
    text: string
  ) =>
    setDeviceInfoList((list) =>
      list.map((item, i) => (i === idx ? { ...item, [field]: text } : item))
    );

  const handleSetNavigation = () => {
    deviceInfoList.forEach(({ key, value }) => {
      if (key && value) Dengage.setInAppDeviceInfo(key, value);
    });
    Dengage.setNavigationWithName(screenName);
  };

  const handleClearDeviceInfo = () => {
    Dengage.clearInAppDeviceInfo();
    setDeviceInfoList([{ key: '', value: '' }]);
  };

  return (
    <KeyboardAvoidingView
      style={styles.flex}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <ScrollView contentContainerStyle={styles.container}>
        <NoCapTextInput
          placeholder="Screen Name"
          value={screenName}
          onChangeText={setScreenName}
          style={styles.input}
        />

        <View style={styles.buttonColumn}>
          <Button title="Set Navigation" onPress={handleSetNavigation} />
          <View style={{ height: 10 }} />
          <Button title="Clear Device Info" onPress={handleClearDeviceInfo} />
        </View>

        <View style={styles.deviceInfoContainer}>
          <Text style={styles.deviceInfoTitle}>Device Info</Text>
          {deviceInfoList.map((info, idx) => (
            <DeviceInfoItemCell
              key={idx}
              keyName={info.key}
              valueName={info.value}
              onChangeKey={(text) => updateDeviceInfo(idx, 'key', text)}
              onChangeValue={(text) => updateDeviceInfo(idx, 'value', text)}
            />
          ))}
          <View style={styles.deviceInfoButtonRow}>
            <TouchableOpacity
              onPress={() =>
                setDeviceInfoList((list) => [...list, { key: '', value: '' }])
              }
              style={styles.deviceInfoSmallButton}
            >
              <Text style={styles.deviceInfoSmallButtonText}>
                + Add New Device Info
              </Text>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={handleClearDeviceInfo}
              style={[
                styles.deviceInfoSmallButton,
                { backgroundColor: '#ffeaea' },
              ]}
            >
              <Text
                style={[styles.deviceInfoSmallButtonText, { color: '#d00' }]}
              >
                Clear Device Info
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1 },
  container: {
    padding: 16,
    backgroundColor: '#fff',
  },
  input: {
    marginBottom: 12,
  },
  buttonColumn: {
    flexDirection: 'column',
    marginBottom: 18,
  },
  deviceInfoContainer: {
    backgroundColor: '#f0f0f0',
    borderRadius: 8,
    padding: 12,
    marginTop: 10,
  },
  deviceInfoTitle: {
    fontWeight: 'bold',
    marginBottom: 8,
    color: '#222',
  },
  deviceInfoButtonRow: {
    flexDirection: 'row',
    gap: 10,
    marginTop: 12,
    justifyContent: 'flex-end',
  },
  deviceInfoSmallButton: {
    paddingVertical: 8,
    paddingHorizontal: 12,
    backgroundColor: '#d3d3d3',
    borderRadius: 5,
    alignItems: 'center',
    minWidth: 0,
  },
  deviceInfoSmallButtonText: {
    color: '#007bff',
    fontWeight: 'bold',
    fontSize: 13,
  },
});
