import React, { useEffect, useState } from 'react';
import {
  SafeAreaView,
  View,
  Text,
  TextInput,
  Button,
  FlatList,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
  Alert,
} from 'react-native';
import Dengage from '@dengage-tech/react-native-dengage';

interface DeviceInfoItem {
  key: string;
  value: string;
}

export default function InAppMessageScreen() {
  const [screenName, setScreenName] = useState<string>('');
  const [deviceInfoList, setDeviceInfoList] = useState<DeviceInfoItem[]>([]);

  useEffect(() => {
    /*
    const info = Dengage.getInAppDeviceInfo?.();
    if (info && typeof info === 'object') {
      const entries = Object.entries(info).map(
        ([k, v]) => ({ key: k, value: v })
      );
      setDeviceInfoList([...entries, { key: '', value: '' }]);
    } else {
      setDeviceInfoList([{ key: '', value: '' }]);
    }
      */
  }, []);

  const updateRow = (
    index: number,
    field: keyof DeviceInfoItem,
    text: string
  ) => {
    /*
    setDeviceInfoList(prev => {
      const newList = [...prev];
      newList[index] = {
        ...newList[index],
        [field]: text,
      };
      return newList;
    });
    */
  };

  const addNewDeviceInfoRow = () => {
    setDeviceInfoList(prev => [...prev, { key: '', value: '' }]);
  };

  const clearDeviceInfo = () => {
    //Dengage.clearInAppDeviceInfo?.();
    setDeviceInfoList([{ key: '', value: '' }]);
  };

  const saveDeviceInfo = () => {
    deviceInfoList.forEach(item => {
      if (item.key.trim() && item.value.trim()) {
        //Dengage.setInAppDeviceInfo?.(item.key.trim(), item.value.trim());
      }
    });
  };

  const onSetNavigation = () => {
    saveDeviceInfo();
    Dengage.setNavigationWithName?.(screenName.trim());
  };

  const renderItem = ({ item, index }: { item: DeviceInfoItem; index: number }) => (
    <View style={styles.row}>
      <TextInput
        style={styles.inputKey}
        placeholder="Key"
        value={item.key}
        onChangeText={text => updateRow(index, 'key', text)}
      />
      <TextInput
        style={styles.inputValue}
        placeholder="Value"
        value={item.value}
        onChangeText={text => updateRow(index, 'value', text)}
      />
    </View>
  );

  return (
    <SafeAreaView style={styles.flex}>
      <KeyboardAvoidingView
        style={styles.flex}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      >
        <View style={styles.container}>
          <TextInput
            style={styles.screenInput}
            placeholder="Screen Name"
            value={screenName}
            onChangeText={setScreenName}
            autoCapitalize="none"
          />
          <Button title="Set Navigation" onPress={onSetNavigation} />
          {/* <View style={styles.buttonMargin}>
            <Button title="Clear Device Info" onPress={clearDeviceInfo} />
          </View>
          <View style={styles.buttonMargin}>
            <Button title="Add New Device Info" onPress={addNewDeviceInfoRow} />
          </View>
          <FlatList
            data={deviceInfoList}
            keyExtractor={(_, idx) => idx.toString()}
            renderItem={renderItem}
            contentContainerStyle={styles.list}
          /> */}
        </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1 },
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#fff',
  },
  screenInput: {
    height: 48,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 12,
    fontSize: 16,
    marginBottom: 12,
  },
  buttonMargin: {
    marginTop: 12,
  },
  list: {
    marginTop: 20,
  },
  row: {
    flexDirection: 'row',
    marginBottom: 12,
  },
  inputKey: {
    flex: 1,
    height: 40,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 8,
    marginRight: 8,
  },
  inputValue: {
    flex: 1,
    height: 40,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 8,
  },
});
