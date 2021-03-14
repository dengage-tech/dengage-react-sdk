import { NativeModules } from 'react-native';

type DengageType = {
  multiply(a: number, b: number): Promise<number>;
};

const { Dengage } = NativeModules;

export default Dengage as DengageType;
