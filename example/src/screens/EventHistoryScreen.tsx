import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  KeyboardAvoidingView,
  Platform,
  Alert,
  Modal,
  FlatList,
} from 'react-native';
import NoCapTextInput from '../components/NoCapTextInput';
import Dengage, { type SdkParameters, type EventMapping } from '@dengage-tech/react-native-dengage';

type EventParameter = {
  key: string;
  value: string;
  isReadOnly: boolean;
  inputType: 'TEXT' | 'DROPDOWN';
  options: string[];
};

type EventTypeConfig = {
  tableName: string;
  parameters: EventParameter[];
};

const EventHistoryScreen: React.FC = () => {
  const [, setSdkParameters] = useState<SdkParameters | null>(null);
  const [eventTypesMap, setEventTypesMap] = useState<Map<string, EventTypeConfig>>(
    new Map()
  );
  const [selectedEventType, setSelectedEventType] = useState<string>('');
  const [eventParameters, setEventParameters] = useState<EventParameter[]>([]);
  const [eventTypes, setEventTypes] = useState<string[]>([]);
  const [showEventTypePicker, setShowEventTypePicker] = useState(false);
  const [showDropdownPicker, setShowDropdownPicker] = useState<number | null>(null);

  useEffect(() => {
    loadSdkParameters();
  }, []);

  useEffect(() => {
    if (selectedEventType && eventTypesMap.has(selectedEventType)) {
      const config = eventTypesMap.get(selectedEventType)!;
      setEventParameters(
        config.parameters.map((p) => ({
          key: p.key,
          value: p.value,
          isReadOnly: p.isReadOnly,
          inputType: p.inputType,
          options: p.options,
        }))
      );
    }
  }, [selectedEventType, eventTypesMap]);

  const loadSdkParameters = async () => {
    try {
      const params = await Dengage.getSdkParameters();
      setSdkParameters(params);
      if (params) {
        processEventMappings(params.eventMappings || []);
      }
    } catch (error) {
      Alert.alert('Error', 'Failed to load SDK parameters');
      console.error('Error loading SDK parameters:', error);
    }
  };

  const processEventMappings = (eventMappings: EventMapping[]) => {
    const systemAttributes = new Set(['event_time', 'device_id', 'session_id']);
    const newEventTypesMap = new Map<string, EventTypeConfig>();

    eventMappings.forEach((eventMapping) => {
      const tableName = eventMapping.eventTableName || '';

      eventMapping.eventTypeDefinitions?.forEach((eventTypeDef) => {
        const eventType = eventTypeDef.eventType;
        if (!eventType || !tableName) return;

        const parameters: EventParameter[] = [];

        // Add filter condition parameters first
        eventTypeDef.filterConditions?.forEach((filterCondition) => {
          const fieldName = filterCondition.fieldName;
          if (!fieldName || systemAttributes.has(fieldName)) return;

          if (filterCondition.operator === 'Equals') {
            const value = filterCondition.values?.[0] || '';
            parameters.push({
              key: fieldName,
              value: value,
              isReadOnly: true,
              inputType: 'TEXT',
              options: [],
            });
          } else if (filterCondition.operator === 'In') {
            const values = filterCondition.values || [];
            const defaultValue = values[0] || '';
            parameters.push({
              key: fieldName,
              value: defaultValue,
              isReadOnly: false,
              inputType: 'DROPDOWN',
              options: values,
            });
          }
        });

        // Add regular attribute parameters
        eventTypeDef.attributes?.forEach((attribute) => {
          const tableColumnName = attribute.tableColumnName;
          if (
            tableColumnName &&
            !systemAttributes.has(tableColumnName) &&
            !parameters.some((p) => p.key === tableColumnName)
          ) {
            parameters.push({
              key: tableColumnName,
              value: '',
              isReadOnly: false,
              inputType: 'TEXT',
              options: [],
            });
          }
        });

        newEventTypesMap.set(eventType, {
          tableName: tableName,
          parameters: parameters,
        });
      });
    });

    setEventTypesMap(newEventTypesMap);
    const types = Array.from(newEventTypesMap.keys());
    setEventTypes(types);
    if (types.length > 0) {
      setSelectedEventType(types[0]);
    }
  };

  const handleAddParameter = () => {
    setEventParameters([
      ...eventParameters,
      {
        key: '',
        value: '',
        isReadOnly: false,
        inputType: 'TEXT',
        options: [],
      },
    ]);
  };

  const handleParameterChange = (
    index: number,
    field: 'key' | 'value',
    text: string
  ) => {
    const updated = [...eventParameters];
    updated[index] = { ...updated[index], [field]: text };
    setEventParameters(updated);
  };

  const handleRemoveParameter = (index: number) => {
    if (eventParameters.length > 1) {
      const updated = eventParameters.filter((_, i) => i !== index);
      setEventParameters(updated);
    }
  };

  const handleSendEvent = () => {
    if (!selectedEventType) {
      Alert.alert('Error', 'Please select an event type');
      return;
    }

    const config = eventTypesMap.get(selectedEventType);
    if (!config) {
      Alert.alert('Error', 'Invalid event type');
      return;
    }

    const eventData: Record<string, any> = {};
    eventParameters.forEach((param) => {
      if (param.key && param.value) {
        eventData[param.key] = param.value;
      }
    });

    Dengage.sendDeviceEvent(config.tableName, eventData);
    Alert.alert('Success', `Event sent to table: ${config.tableName}`);
  };

  const getTableName = () => {
    const config = eventTypesMap.get(selectedEventType);
    return config?.tableName || '';
  };

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
      <ScrollView contentContainerStyle={styles.container}>
        <Text style={styles.label}>Table Name: {getTableName()}</Text>

        {eventTypes.length > 0 ? (
          <>
            <Text style={styles.label}>Event Type:</Text>
            <TouchableOpacity
              style={styles.pickerContainer}
              onPress={() => setShowEventTypePicker(true)}>
              <Text style={styles.pickerText}>
                {selectedEventType || 'Select Event Type'}
              </Text>
            </TouchableOpacity>
            <Modal
              visible={showEventTypePicker}
              transparent={true}
              animationType="slide"
              onRequestClose={() => setShowEventTypePicker(false)}>
              <View style={styles.modalOverlay}>
                <View style={styles.modalContent}>
                  <Text style={styles.modalTitle}>Select Event Type</Text>
                  <FlatList
                    data={eventTypes}
                    keyExtractor={(item) => item}
                    renderItem={({ item }) => (
                      <TouchableOpacity
                        style={styles.modalItem}
                        onPress={() => {
                          setSelectedEventType(item);
                          setShowEventTypePicker(false);
                        }}>
                        <Text style={styles.modalItemText}>{item}</Text>
                      </TouchableOpacity>
                    )}
                  />
                  <TouchableOpacity
                    style={styles.modalCloseButton}
                    onPress={() => setShowEventTypePicker(false)}>
                    <Text style={styles.modalCloseButtonText}>Close</Text>
                  </TouchableOpacity>
                </View>
              </View>
            </Modal>
          </>
        ) : (
          <Text style={styles.emptyText}>No event types available</Text>
        )}

        <TouchableOpacity style={styles.addButton} onPress={handleAddParameter}>
          <Text style={styles.addButtonText}>+ Add Parameter</Text>
        </TouchableOpacity>

        {eventParameters.map((param, index) => (
          <View key={index} style={styles.paramRow}>
            <NoCapTextInput
              style={[styles.input, { flex: 1 }]}
              placeholder="Key"
              value={param.key}
              onChangeText={(text) => handleParameterChange(index, 'key', text)}
              editable={!param.isReadOnly}
            />
            {param.inputType === 'DROPDOWN' ? (
              <>
                <TouchableOpacity
                  style={[styles.input, { flex: 1, marginLeft: 8 }]}
                  onPress={() => setShowDropdownPicker(index)}>
                  <Text style={styles.pickerText}>
                    {param.value || 'Select Value'}
                  </Text>
                </TouchableOpacity>
                <Modal
                  visible={showDropdownPicker === index}
                  transparent={true}
                  animationType="slide"
                  onRequestClose={() => setShowDropdownPicker(null)}>
                  <View style={styles.modalOverlay}>
                    <View style={styles.modalContent}>
                      <Text style={styles.modalTitle}>Select Value</Text>
                      <FlatList
                        data={param.options}
                        keyExtractor={(item) => item}
                        renderItem={({ item }) => (
                          <TouchableOpacity
                            style={styles.modalItem}
                            onPress={() => {
                              handleParameterChange(index, 'value', item);
                              setShowDropdownPicker(null);
                            }}>
                            <Text style={styles.modalItemText}>{item}</Text>
                          </TouchableOpacity>
                        )}
                      />
                      <TouchableOpacity
                        style={styles.modalCloseButton}
                        onPress={() => setShowDropdownPicker(null)}>
                        <Text style={styles.modalCloseButtonText}>Close</Text>
                      </TouchableOpacity>
                    </View>
                  </View>
                </Modal>
              </>
            ) : (
              <NoCapTextInput
                style={[styles.input, { flex: 1, marginLeft: 8 }]}
                placeholder="Value"
                value={param.value}
                onChangeText={(text) =>
                  handleParameterChange(index, 'value', text)
                }
                editable={!param.isReadOnly}
              />
            )}
            {eventParameters.length > 1 && (
              <TouchableOpacity
                style={styles.removeButton}
                onPress={() => handleRemoveParameter(index)}>
                <Text style={styles.removeButtonText}>Remove</Text>
              </TouchableOpacity>
            )}
          </View>
        ))}

        <TouchableOpacity style={styles.sendButton} onPress={handleSendEvent}>
          <Text style={styles.sendButtonText}>Send Event</Text>
        </TouchableOpacity>
      </ScrollView>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 16,
    backgroundColor: '#fff',
    flexGrow: 1,
  },
  label: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 8,
    marginTop: 8,
  },
  pickerContainer: {
    borderWidth: 1,
    borderColor: '#bbb',
    borderRadius: 6,
    marginBottom: 10,
    backgroundColor: '#fff',
    padding: 12,
    justifyContent: 'center',
  },
  pickerText: {
    fontSize: 16,
    color: '#222',
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  modalContent: {
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 20,
    width: '80%',
    maxHeight: '60%',
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  modalItem: {
    padding: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
  },
  modalItemText: {
    fontSize: 16,
  },
  modalCloseButton: {
    marginTop: 16,
    padding: 12,
    backgroundColor: '#007bff',
    borderRadius: 6,
    alignItems: 'center',
  },
  modalCloseButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  emptyText: {
    fontSize: 14,
    color: '#666',
    marginBottom: 16,
  },
  input: {
    borderWidth: 1,
    borderColor: '#bbb',
    borderRadius: 6,
    padding: 8,
    backgroundColor: '#fff',
    color: '#222',
  },
  paramRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  addButton: {
    marginBottom: 10,
    alignSelf: 'flex-end',
  },
  addButtonText: {
    color: '#007bff',
    fontWeight: 'bold',
    fontSize: 15,
  },
  removeButton: {
    marginLeft: 8,
    padding: 8,
  },
  removeButtonText: {
    color: '#dc3545',
    fontWeight: 'bold',
  },
  sendButton: {
    backgroundColor: '#007bff',
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 16,
  },
  sendButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default EventHistoryScreen;

