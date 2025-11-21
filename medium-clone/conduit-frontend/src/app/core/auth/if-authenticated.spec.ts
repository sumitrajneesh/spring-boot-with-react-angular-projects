import { IfAuthenticated } from './if-authenticated';

describe('IfAuthenticated', () => {
  it('should create an instance', () => {
    const directive = new IfAuthenticated();
    expect(directive).toBeTruthy();
  });
});
