import { useState } from 'react';
import {
  View,
  Button,
  Text,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  KeyboardAvoidingView,
  Platform,
} from 'react-native';
import NoCapTextInput from '../components/NoCapTextInput';
import Dengage from '@dengage-tech/react-native-dengage';

type Param = { key: string; value: string };

function CustomFunctionInput({
  placeholder,
  value,
  onChangeText,
  onSet,
}: {
  placeholder: string;
  value: string;
  onChangeText: (text: string) => void;
  onSet: () => void;
}) {
  return (
    <View style={styles.customFunctionRow}>
      <NoCapTextInput
        style={[styles.input, { flex: 1 }]}
        placeholder={placeholder}
        value={value}
        onChangeText={onChangeText}
      />
      <View style={{ marginLeft: 8 }}>
        <Button title="Set" onPress={onSet} color="#007AFF" />
      </View>
    </View>
  );
}

export default function RTInAppMessagesScreen() {
  const [screenName, setScreenName] = useState('');
  const [params, setParams] = useState<Param[]>([]);
  const [categoryPath, setCategoryPath] = useState('');
  const [cartItemCount, setCartItemCount] = useState('');
  const [cartAmount, setCartAmount] = useState('');
  const [state, setState] = useState('');
  const [city, setCity] = useState('');

  const handleAddParam = () => {
    setParams([...params, { key: '', value: '' }]);
  };

  const handleParamChange = (idx: number, field: 'key' | 'value', text: string) => {
    setParams((prev) =>
      prev.map((item, i) => (i === idx ? { ...item, [field]: text } : item))
    );
  };

  const handleSetCategoryPath = () => {
    if (categoryPath) Dengage.setCategoryPath(categoryPath);
  };
  const handleSetCartItemCount = () => {
    if (cartItemCount) Dengage.setCartItemCount(cartItemCount);
  };
  const handleSetCartAmount = () => {
    if (cartAmount) Dengage.setCartAmount(cartAmount);
  };
  const handleSetState = () => {
    if (state) Dengage.setState(state);
  };
  const handleSetCity = () => {
    if (city) Dengage.setCity(city);
  };

  const handleShow = () => {
    const paramObj: Record<string, string> = {};
    params.forEach(({ key, value }) => {
      if (key && value) paramObj[key] = value;
    });
    Dengage.showRealTimeInApp(screenName, paramObj);
  };

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <ScrollView contentContainerStyle={styles.container}>
        <CustomFunctionInput
          placeholder="Category Path"
          value={categoryPath}
          onChangeText={setCategoryPath}
          onSet={handleSetCategoryPath}
        />
        <CustomFunctionInput
          placeholder="Cart Item Count"
          value={cartItemCount}
          onChangeText={setCartItemCount}
          onSet={handleSetCartItemCount}
        />
        <CustomFunctionInput
          placeholder="Cart Amount"
          value={cartAmount}
          onChangeText={setCartAmount}
          onSet={handleSetCartAmount}
        />
        <CustomFunctionInput
          placeholder="State"
          value={state}
          onChangeText={setState}
          onSet={handleSetState}
        />
        <CustomFunctionInput
          placeholder="City"
          value={city}
          onChangeText={setCity}
          onSet={handleSetCity}
        />

        <NoCapTextInput
          style={styles.input}
          placeholder="Screen name"
          value={screenName}
          onChangeText={setScreenName}
        />

        <TouchableOpacity style={styles.addParamBtn} onPress={handleAddParam}>
          <Text style={styles.addParamBtnText}>+ Add New Parameter</Text>
        </TouchableOpacity>

        {params.map((param, idx) => (
          <View key={idx} style={styles.paramRow}>
            <NoCapTextInput
              style={[styles.input, { flex: 1 }]}
              placeholder="Key"
              value={param.key}
              onChangeText={(text) => handleParamChange(idx, 'key', text)}
            />
            <NoCapTextInput
              style={[styles.input, { flex: 1, marginLeft: 8 }]}
              placeholder="Value"
              value={param.value}
              onChangeText={(text) => handleParamChange(idx, 'value', text)}
            />
          </View>
        ))}

        <Button title="Show Real Time In App" onPress={handleShow} />
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 16,
    backgroundColor: '#fff',
    flexGrow: 1,
  },
  input: {
    borderWidth: 1,
    borderColor: '#bbb',
    borderRadius: 6,
    padding: 8,
    marginBottom: 10,
    backgroundColor: '#fff',
    color: '#222',
  },
  customFunctionRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 10,
  },
  addParamBtn: {
    marginBottom: 10,
    alignSelf: 'flex-end',
  },
  addParamBtnText: {
    color: '#007bff',
    fontWeight: 'bold',
    fontSize: 15,
  },
  paramRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
});