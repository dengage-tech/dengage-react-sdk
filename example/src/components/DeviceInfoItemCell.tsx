import { View, StyleSheet } from 'react-native';
import NoCapTextInput from './NoCapTextInput';

const DeviceInfoItemCell = ({
  keyName,
  valueName,
  onChangeKey,
  onChangeValue,
}: {
  keyName: string;
  valueName: string;
  onChangeKey: (text: string) => void;
  onChangeValue: (text: string) => void;
}) => (
  <View style={styles.deviceInfoRow}>
    <NoCapTextInput
      style={[styles.deviceInfoInput, { marginRight: 8 }]}
      placeholder="Key"
      value={keyName}
      onChangeText={onChangeKey}
    />
    <NoCapTextInput
      style={styles.deviceInfoInput}
      placeholder="Value"
      value={valueName}
      onChangeText={onChangeValue}
    />
  </View>
);

const styles = StyleSheet.create({
  deviceInfoRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
    justifyContent: 'center',
  },
  deviceInfoInput: {
    flex: 1,
    backgroundColor: '#fff',
    borderRadius: 6,
    borderWidth: 1,
    borderColor: '#ccc',
    paddingHorizontal: 8,
    height: 40,
    textAlign: 'center',
    minWidth: 0,
  },
});

export default DeviceInfoItemCell;
