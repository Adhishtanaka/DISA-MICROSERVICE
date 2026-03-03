import React, { useState } from 'react';
import type {
  Person,
  PersonRequest,
  PersonnelStatus,
  Skill,
  EmergencyContact,
  MedicalCondition,
} from '../types/personnel.types';

interface PersonnelFormProps {
  person?: Person | null;
  onSubmit: (data: PersonRequest) => Promise<void>;
  onCancel: () => void;
  onDelete?: (id: number) => Promise<void>;
}

const STATUSES: PersonnelStatus[] = ['Available', 'On Duty', 'On Leave', 'Inactive'];

const emptySkill = (): Skill => ({
  profession: '',
  experienceYears: 0,
  missionCount: 0,
  level: 'Beginner',
});

const emptyContact = (): EmergencyContact => ({
  name: '',
  telephone: '',
  address: '',
  relation: '',
  note: '',
});

const emptyMedical = (): MedicalCondition => ({
  bloodGroup: '',
  height: '',
  weight: '',
  allergies: [],
  chronicConditions: [],
  physicalLimitations: [],
  medications: [],
  pastInjuries: [],
});

export function PersonnelForm({ person, onSubmit, onCancel, onDelete }: PersonnelFormProps) {
  const isEdit = !!person;

  const [firstName, setFirstName] = useState(person?.firstName ?? '');
  const [lastName, setLastName] = useState(person?.lastName ?? '');
  const [phone, setPhone] = useState(person?.phone ?? '');
  const [email, setEmail] = useState(person?.email ?? '');
  const [address, setAddress] = useState(person?.address ?? '');
  const [role, setRole] = useState(person?.role ?? '');
  const [department, setDepartment] = useState(person?.department ?? '');
  const [organization, setOrganization] = useState(person?.organization ?? '');
  const [rank, setRank] = useState(person?.rank ?? '');
  const [status, setStatus] = useState<PersonnelStatus>(person?.status ?? 'Available');
  const [shiftStart, setShiftStart] = useState(person?.shiftStartTime?.slice(0, 16) ?? '');
  const [shiftEnd, setShiftEnd] = useState(person?.shiftEndTime?.slice(0, 16) ?? '');

  const [skills, setSkills] = useState<Skill[]>(
    person?.skills?.length ? person.skills : []
  );
  const [contacts, setContacts] = useState<EmergencyContact[]>(
    person?.emergencyContacts?.length ? person.emergencyContacts : []
  );
  const [medical, setMedical] = useState<MedicalCondition>(
    person?.medicalCondition ?? emptyMedical()
  );

  const [loading, setLoading] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // ── Section toggle state ──
  const [showSkills, setShowSkills] = useState(false);
  const [showContacts, setShowContacts] = useState(false);
  const [showMedical, setShowMedical] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const payload: PersonRequest = {
        ...(isEdit && person ? { id: person.id, personalCode: person.personalCode } : {}),
        firstName,
        lastName,
        phone,
        email,
        address,
        role,
        department,
        organization,
        rank,
        status,
        shiftStartTime: shiftStart || null,
        shiftEndTime: shiftEnd || null,
        skills,
        emergencyContacts: contacts,
        medicalCondition: medical.bloodGroup || medical.height || medical.weight ? medical : null,
      };
      await onSubmit(payload);
    } catch (err: any) {
      setError(err.response?.data?.message || err.message || 'Operation failed');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!person || !onDelete) return;
    if (!window.confirm(`Delete ${person.firstName} ${person.lastName}?`)) return;
    setDeleting(true);
    try {
      await onDelete(person.id);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Delete failed');
      setDeleting(false);
    }
  };

  // ── Skill helpers ──
  const addSkill = () => setSkills([...skills, emptySkill()]);
  const removeSkill = (i: number) => setSkills(skills.filter((_, idx) => idx !== i));
  const updateSkill = (i: number, field: keyof Skill, value: string | number) =>
    setSkills(skills.map((s, idx) => (idx === i ? { ...s, [field]: value } : s)));

  // ── Contact helpers ──
  const addContact = () => setContacts([...contacts, emptyContact()]);
  const removeContact = (i: number) => setContacts(contacts.filter((_, idx) => idx !== i));
  const updateContact = (i: number, field: keyof EmergencyContact, value: string) =>
    setContacts(contacts.map((c, idx) => (idx === i ? { ...c, [field]: value } : c)));

  const inputCls =
    'w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm';
  const labelCls = 'block text-sm font-medium text-gray-700 mb-1';

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg max-w-4xl w-full max-h-[90vh] overflow-y-auto">
        <div className="p-6">
          {/* Header */}
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-900">
              {isEdit ? 'Update Personnel' : 'Add New Personnel'}
            </h2>
            <div className="flex items-center gap-2">
              {isEdit && onDelete && (
                <button
                  type="button"
                  onClick={handleDelete}
                  disabled={deleting}
                  className="px-3 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 disabled:bg-gray-300 transition-colors"
                >
                  {deleting ? 'Deleting...' : 'Delete'}
                </button>
              )}
              <button onClick={onCancel} className="text-gray-400 hover:text-gray-600 text-2xl">
                &times;
              </button>
            </div>
          </div>

          {error && (
            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded text-red-700 text-sm">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            {/* ── Personal Information ── */}
            <fieldset className="border border-gray-200 rounded-lg p-4">
              <legend className="text-sm font-semibold text-gray-800 px-2">
                Personal Information
              </legend>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className={labelCls}>First Name *</label>
                  <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} required className={inputCls} />
                </div>
                <div>
                  <label className={labelCls}>Last Name *</label>
                  <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} required className={inputCls} />
                </div>
                <div>
                  <label className={labelCls}>Phone *</label>
                  <input type="tel" value={phone} onChange={(e) => setPhone(e.target.value)} required className={inputCls} />
                </div>
                <div>
                  <label className={labelCls}>Email *</label>
                  <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required className={inputCls} />
                </div>
                <div className="md:col-span-2">
                  <label className={labelCls}>Address *</label>
                  <input type="text" value={address} onChange={(e) => setAddress(e.target.value)} required className={inputCls} />
                </div>
              </div>
            </fieldset>

            {/* ── Organization Details ── */}
            <fieldset className="border border-gray-200 rounded-lg p-4">
              <legend className="text-sm font-semibold text-gray-800 px-2">
                Organization Details
              </legend>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className={labelCls}>Role *</label>
                  <input type="text" value={role} onChange={(e) => setRole(e.target.value)} required placeholder="Rescue Officer" className={inputCls} />
                </div>
                <div>
                  <label className={labelCls}>Department *</label>
                  <input type="text" value={department} onChange={(e) => setDepartment(e.target.value)} required placeholder="Search & Rescue" className={inputCls} />
                </div>
                <div>
                  <label className={labelCls}>Organization *</label>
                  <input type="text" value={organization} onChange={(e) => setOrganization(e.target.value)} required placeholder="NDMA" className={inputCls} />
                </div>
                <div>
                  <label className={labelCls}>Rank</label>
                  <input type="text" value={rank} onChange={(e) => setRank(e.target.value)} placeholder="Senior" className={inputCls} />
                </div>
                <div>
                  <label className={labelCls}>Status *</label>
                  <select value={status} onChange={(e) => setStatus(e.target.value as PersonnelStatus)} required className={inputCls}>
                    {STATUSES.map((s) => (
                      <option key={s} value={s}>{s}</option>
                    ))}
                  </select>
                </div>
              </div>
            </fieldset>

            {/* ── Shift Schedule ── */}
            <fieldset className="border border-gray-200 rounded-lg p-4">
              <legend className="text-sm font-semibold text-gray-800 px-2">
                Shift Schedule
              </legend>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className={labelCls}>Shift Start</label>
                  <input type="datetime-local" value={shiftStart} onChange={(e) => setShiftStart(e.target.value)} className={inputCls} />
                </div>
                <div>
                  <label className={labelCls}>Shift End</label>
                  <input type="datetime-local" value={shiftEnd} onChange={(e) => setShiftEnd(e.target.value)} className={inputCls} />
                </div>
              </div>
            </fieldset>

            {/* ── Skills (collapsible) ── */}
            <fieldset className="border border-gray-200 rounded-lg p-4">
              <legend
                className="text-sm font-semibold text-gray-800 px-2 cursor-pointer select-none"
                onClick={() => setShowSkills(!showSkills)}
              >
                Skills ({skills.length}) {showSkills ? '▾' : '▸'}
              </legend>
              {showSkills && (
                <div className="space-y-3 mt-2">
                  {skills.map((s, i) => (
                    <div key={i} className="grid grid-cols-2 md:grid-cols-5 gap-2 items-end bg-gray-50 p-3 rounded">
                      <div>
                        <label className={labelCls}>Profession</label>
                        <input type="text" value={s.profession} onChange={(e) => updateSkill(i, 'profession', e.target.value)} className={inputCls} placeholder="Paramedic" />
                      </div>
                      <div>
                        <label className={labelCls}>Experience (yrs)</label>
                        <input type="number" value={s.experienceYears} onChange={(e) => updateSkill(i, 'experienceYears', parseInt(e.target.value) || 0)} min="0" className={inputCls} />
                      </div>
                      <div>
                        <label className={labelCls}>Missions</label>
                        <input type="number" value={s.missionCount} onChange={(e) => updateSkill(i, 'missionCount', parseInt(e.target.value) || 0)} min="0" className={inputCls} />
                      </div>
                      <div>
                        <label className={labelCls}>Level</label>
                        <select value={s.level} onChange={(e) => updateSkill(i, 'level', e.target.value)} className={inputCls}>
                          <option value="Beginner">Beginner</option>
                          <option value="Intermediate">Intermediate</option>
                          <option value="Advanced">Advanced</option>
                          <option value="Expert">Expert</option>
                        </select>
                      </div>
                      <div>
                        <button type="button" onClick={() => removeSkill(i)} className="px-3 py-2 text-red-600 hover:bg-red-50 rounded text-sm">Remove</button>
                      </div>
                    </div>
                  ))}
                  <button type="button" onClick={addSkill} className="text-sm text-blue-600 hover:text-blue-800 font-medium">
                    + Add Skill
                  </button>
                </div>
              )}
            </fieldset>

            {/* ── Emergency Contacts (collapsible) ── */}
            <fieldset className="border border-gray-200 rounded-lg p-4">
              <legend
                className="text-sm font-semibold text-gray-800 px-2 cursor-pointer select-none"
                onClick={() => setShowContacts(!showContacts)}
              >
                Emergency Contacts ({contacts.length}) {showContacts ? '▾' : '▸'}
              </legend>
              {showContacts && (
                <div className="space-y-3 mt-2">
                  {contacts.map((c, i) => (
                    <div key={i} className="grid grid-cols-2 md:grid-cols-3 gap-2 items-end bg-gray-50 p-3 rounded">
                      <div>
                        <label className={labelCls}>Name</label>
                        <input type="text" value={c.name} onChange={(e) => updateContact(i, 'name', e.target.value)} className={inputCls} />
                      </div>
                      <div>
                        <label className={labelCls}>Telephone</label>
                        <input type="tel" value={c.telephone} onChange={(e) => updateContact(i, 'telephone', e.target.value)} className={inputCls} />
                      </div>
                      <div>
                        <label className={labelCls}>Relation</label>
                        <input type="text" value={c.relation} onChange={(e) => updateContact(i, 'relation', e.target.value)} className={inputCls} />
                      </div>
                      <div>
                        <label className={labelCls}>Address</label>
                        <input type="text" value={c.address} onChange={(e) => updateContact(i, 'address', e.target.value)} className={inputCls} />
                      </div>
                      <div>
                        <label className={labelCls}>Note</label>
                        <input type="text" value={c.note} onChange={(e) => updateContact(i, 'note', e.target.value)} className={inputCls} />
                      </div>
                      <div>
                        <button type="button" onClick={() => removeContact(i)} className="px-3 py-2 text-red-600 hover:bg-red-50 rounded text-sm">Remove</button>
                      </div>
                    </div>
                  ))}
                  <button type="button" onClick={addContact} className="text-sm text-blue-600 hover:text-blue-800 font-medium">
                    + Add Contact
                  </button>
                </div>
              )}
            </fieldset>

            {/* ── Medical Condition (collapsible) ── */}
            <fieldset className="border border-gray-200 rounded-lg p-4">
              <legend
                className="text-sm font-semibold text-gray-800 px-2 cursor-pointer select-none"
                onClick={() => setShowMedical(!showMedical)}
              >
                Medical Condition {showMedical ? '▾' : '▸'}
              </legend>
              {showMedical && (
                <div className="space-y-4 mt-2">
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                      <label className={labelCls}>Blood Group</label>
                      <select
                        value={medical.bloodGroup}
                        onChange={(e) => setMedical({ ...medical, bloodGroup: e.target.value })}
                        className={inputCls}
                      >
                        <option value="">Select</option>
                        {['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'].map((bg) => (
                          <option key={bg} value={bg}>{bg}</option>
                        ))}
                      </select>
                    </div>
                    <div>
                      <label className={labelCls}>Height (cm)</label>
                      <input type="text" value={medical.height} onChange={(e) => setMedical({ ...medical, height: e.target.value })} placeholder="175" className={inputCls} />
                    </div>
                    <div>
                      <label className={labelCls}>Weight (kg)</label>
                      <input type="text" value={medical.weight} onChange={(e) => setMedical({ ...medical, weight: e.target.value })} placeholder="70" className={inputCls} />
                    </div>
                  </div>

                  {/* Allergies */}
                  <div>
                    <div className="flex items-center justify-between mb-2">
                      <span className="text-sm font-medium text-gray-700">Allergies</span>
                      <button
                        type="button"
                        onClick={() =>
                          setMedical({
                            ...medical,
                            allergies: [...(medical.allergies ?? []), { type: '', allergyTo: '' }],
                          })
                        }
                        className="text-xs text-blue-600 hover:text-blue-800"
                      >
                        + Add
                      </button>
                    </div>
                    {medical.allergies?.map((a, i) => (
                      <div key={i} className="flex gap-2 mb-2">
                        <input
                          type="text"
                          placeholder="Type (e.g. Food)"
                          value={a.type}
                          onChange={(e) => {
                            const updated = [...(medical.allergies ?? [])];
                            updated[i] = { ...updated[i], type: e.target.value };
                            setMedical({ ...medical, allergies: updated });
                          }}
                          className={inputCls}
                        />
                        <input
                          type="text"
                          placeholder="Allergic to"
                          value={a.allergyTo}
                          onChange={(e) => {
                            const updated = [...(medical.allergies ?? [])];
                            updated[i] = { ...updated[i], allergyTo: e.target.value };
                            setMedical({ ...medical, allergies: updated });
                          }}
                          className={inputCls}
                        />
                        <button
                          type="button"
                          onClick={() =>
                            setMedical({
                              ...medical,
                              allergies: medical.allergies?.filter((_, idx) => idx !== i),
                            })
                          }
                          className="text-red-500 hover:text-red-700 px-2"
                        >
                          &times;
                        </button>
                      </div>
                    ))}
                  </div>

                  {/* Chronic Conditions */}
                  <div>
                    <div className="flex items-center justify-between mb-2">
                      <span className="text-sm font-medium text-gray-700">Chronic Conditions</span>
                      <button
                        type="button"
                        onClick={() =>
                          setMedical({
                            ...medical,
                            chronicConditions: [
                              ...(medical.chronicConditions ?? []),
                              { name: '', severity: 'Mild' },
                            ],
                          })
                        }
                        className="text-xs text-blue-600 hover:text-blue-800"
                      >
                        + Add
                      </button>
                    </div>
                    {medical.chronicConditions?.map((cc, i) => (
                      <div key={i} className="flex gap-2 mb-2">
                        <input
                          type="text"
                          placeholder="Condition name"
                          value={cc.name}
                          onChange={(e) => {
                            const updated = [...(medical.chronicConditions ?? [])];
                            updated[i] = { ...updated[i], name: e.target.value };
                            setMedical({ ...medical, chronicConditions: updated });
                          }}
                          className={inputCls}
                        />
                        <select
                          value={cc.severity}
                          onChange={(e) => {
                            const updated = [...(medical.chronicConditions ?? [])];
                            updated[i] = { ...updated[i], severity: e.target.value };
                            setMedical({ ...medical, chronicConditions: updated });
                          }}
                          className={inputCls}
                        >
                          <option value="Mild">Mild</option>
                          <option value="Moderate">Moderate</option>
                          <option value="Severe">Severe</option>
                        </select>
                        <button
                          type="button"
                          onClick={() =>
                            setMedical({
                              ...medical,
                              chronicConditions: medical.chronicConditions?.filter((_, idx) => idx !== i),
                            })
                          }
                          className="text-red-500 hover:text-red-700 px-2"
                        >
                          &times;
                        </button>
                      </div>
                    ))}
                  </div>

                  {/* Physical Limitations */}
                  <div>
                    <div className="flex items-center justify-between mb-2">
                      <span className="text-sm font-medium text-gray-700">Physical Limitations</span>
                      <button
                        type="button"
                        onClick={() =>
                          setMedical({
                            ...medical,
                            physicalLimitations: [
                              ...(medical.physicalLimitations ?? []),
                              { limitation: '' },
                            ],
                          })
                        }
                        className="text-xs text-blue-600 hover:text-blue-800"
                      >
                        + Add
                      </button>
                    </div>
                    {medical.physicalLimitations?.map((pl, i) => (
                      <div key={i} className="flex gap-2 mb-2">
                        <input
                          type="text"
                          placeholder="Limitation description"
                          value={pl.limitation}
                          onChange={(e) => {
                            const updated = [...(medical.physicalLimitations ?? [])];
                            updated[i] = { ...updated[i], limitation: e.target.value };
                            setMedical({ ...medical, physicalLimitations: updated });
                          }}
                          className={inputCls}
                        />
                        <button
                          type="button"
                          onClick={() =>
                            setMedical({
                              ...medical,
                              physicalLimitations: medical.physicalLimitations?.filter(
                                (_, idx) => idx !== i
                              ),
                            })
                          }
                          className="text-red-500 hover:text-red-700 px-2"
                        >
                          &times;
                        </button>
                      </div>
                    ))}
                  </div>

                  {/* Medications */}
                  <div>
                    <div className="flex items-center justify-between mb-2">
                      <span className="text-sm font-medium text-gray-700">Medications</span>
                      <button
                        type="button"
                        onClick={() =>
                          setMedical({
                            ...medical,
                            medications: [
                              ...(medical.medications ?? []),
                              { name: '', dosage: '', frequency: '' },
                            ],
                          })
                        }
                        className="text-xs text-blue-600 hover:text-blue-800"
                      >
                        + Add
                      </button>
                    </div>
                    {medical.medications?.map((m, i) => (
                      <div key={i} className="flex gap-2 mb-2">
                        <input
                          type="text"
                          placeholder="Medication name"
                          value={m.name}
                          onChange={(e) => {
                            const updated = [...(medical.medications ?? [])];
                            updated[i] = { ...updated[i], name: e.target.value };
                            setMedical({ ...medical, medications: updated });
                          }}
                          className={inputCls}
                        />
                        <input
                          type="text"
                          placeholder="Dosage"
                          value={m.dosage}
                          onChange={(e) => {
                            const updated = [...(medical.medications ?? [])];
                            updated[i] = { ...updated[i], dosage: e.target.value };
                            setMedical({ ...medical, medications: updated });
                          }}
                          className={inputCls}
                        />
                        <input
                          type="text"
                          placeholder="Frequency"
                          value={m.frequency}
                          onChange={(e) => {
                            const updated = [...(medical.medications ?? [])];
                            updated[i] = { ...updated[i], frequency: e.target.value };
                            setMedical({ ...medical, medications: updated });
                          }}
                          className={inputCls}
                        />
                        <button
                          type="button"
                          onClick={() =>
                            setMedical({
                              ...medical,
                              medications: medical.medications?.filter((_, idx) => idx !== i),
                            })
                          }
                          className="text-red-500 hover:text-red-700 px-2"
                        >
                          &times;
                        </button>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </fieldset>

            {/* ── Submit Buttons ── */}
            <div className="flex gap-3 pt-2">
              <button
                type="submit"
                disabled={loading}
                className="flex-1 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
              >
                {loading ? 'Saving...' : isEdit ? 'Update Personnel' : 'Create Personnel'}
              </button>
              <button
                type="button"
                onClick={onCancel}
                className="flex-1 bg-gray-200 text-gray-700 py-2 px-4 rounded-md hover:bg-gray-300 transition-colors"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
